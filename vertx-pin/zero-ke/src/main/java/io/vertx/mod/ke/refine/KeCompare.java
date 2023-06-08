package io.vertx.mod.ke.refine;

import io.horizon.eon.VValue;
import io.horizon.uca.log.Annal;
import io.modello.specification.meta.HMetaAtom;
import io.modello.specification.meta.HMetaField;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.record.Apt;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.vertx.mod.ke.refine.Ke.LOG;

class KeCompare {

    /*
     * ADD / UPDATE
     * insert
     * update
     */
    static Apt compared(final Apt apt, final String fieldName, final String user) {
        final JsonArray inserted = new JsonArray();
        final JsonArray updated = new JsonArray();
        final JsonArray originalArray = new JsonArray();

        final JsonArray original = apt.dataO();
        final JsonArray latest = apt.dataN();

        Ut.itJArray(latest).forEach(each -> {
            final String value = each.getString(fieldName);
            final JsonObject item = Ut.elementFind(original, fieldName, value);
            if (Objects.isNull(item)) {
                final JsonObject record = each.copy();
                record.put(KName.CREATED_AT, Instant.now());
                record.put(KName.CREATED_BY, user);
                inserted.add(record);
            } else {
                final JsonObject dataItem = new JsonObject();
                dataItem.mergeIn(item.copy(), true).mergeIn(each.copy());
                dataItem.put(KName.UPDATED_AT, Instant.now());
                dataItem.put(KName.UPDATED_BY, user);
                originalArray.add(item.copy());
                updated.add(dataItem);
            }
        });
        return Apt.create(originalArray, latest).comparedA(inserted).comparedU(updated);
    }

    static BiFunction<Function<JsonArray, Future<JsonArray>>, Function<JsonArray, Future<JsonArray>>, Future<JsonArray>> atomyFn(
        final Class<?> clazz,
        final Apt compared) {
        return (iFun, uFun) -> {
            final JsonArray inserted = compared.comparedA();
            final JsonArray updated = compared.comparedU();

            final Annal LOGGER = Annal.get(clazz);
            LOG.Ke.info(LOGGER, "Result of calculated, Insert = {0}, Update = {1}",
                String.valueOf(inserted.size()),
                String.valueOf(updated.size()));

            final List<Future<JsonArray>> futures = new ArrayList<>();
            futures.add(Fn.ofJArray(iFun).apply(inserted));
            futures.add(Fn.ofJArray(uFun).apply(updated));
            return Fn.compressA(futures);
        };
    }

    static Future<JsonArray> combineAsync(final JsonArray data, final ConcurrentMap<String, String> headers,
                                          final List<String> columns,
                                          final HMetaAtom metaAtom) {
        final JsonArray combined = new JsonArray();
        final boolean complex;
        if (metaAtom == null) {
            complex = false;
        } else {
            complex = metaAtom.isComplex();
        }
        /*
         * Header
         * To keep the template is the same as importing, here provide some correction
         * The header should has two rows:
         * CnHeader + EnHeader
         *
         * -- CnHeader for label
         * -- EnHeader for field
         * */
        if (complex) {
            /*
             * JsonObject replaced string data here
             */
            final JsonArray firstCnHeader = new JsonArray();
            final JsonArray firstEnHeader = new JsonArray();

            final JsonArray secondCnHeader = new JsonArray();
            final JsonArray secondEnHeader = new JsonArray();

            final Set<String> complexField = new HashSet<>();
            Ut.itList(columns, (column, index) -> {
                /*
                 * New Data Structure
                 */
                if (metaAtom.isComplex(column)) {
                    complexField.add(column);
                    // Complex that belong to data array
                    final int columnSize = metaAtom.size(column);
                    firstCnHeader.add(itemColumn(headers.get(column), columnSize));
                    firstEnHeader.add(itemColumn(column, columnSize));

                    // Children column here
                    final HMetaField item = metaAtom.field(column);
                    if (Objects.nonNull(item)) {
                        /*
                         * Adjust
                         * 1) firstCnHeader
                         * 2) firstEnHeader
                         */
                        final int childCount = item.children().size() - 1;
                        Ut.itRepeat(childCount, () -> {
                            firstCnHeader.addNull();
                            firstEnHeader.addNull();
                        });
                        item.children().forEach(child -> {
                            secondCnHeader.add(child.alias());
                            secondEnHeader.add(child.name());
                        });
                    }
                } else {
                    // Simple column here
                    firstCnHeader.add(itemRow(headers.get(column), 2));
                    firstEnHeader.add(itemRow(column, 2));

                    secondCnHeader.addNull();
                    secondEnHeader.addNull();
                }
            });
            combined.add(firstCnHeader);
            combined.add(secondCnHeader);
            combined.add(firstEnHeader);
            combined.add(secondEnHeader);
            /* Data Part */
            Ut.itJArray(data, (each, index) -> {
                /* Current row max */
                final int max = rowCalculate(each, complexField);
                /* Data Part */
                final JsonArray row = new JsonArray();
                columns.forEach(column -> {
                    if (metaAtom.isComplex(column)) {
                        /* If complex */
                        final JsonArray columnValue = each.getJsonArray(column);
                        /*
                         * children field
                         */
                        final HMetaField item = metaAtom.field(column);
                        /*
                         * Only pick first
                         */
                        if (Ut.isNotNil(columnValue)) {
                            final JsonObject value = columnValue.getJsonObject(VValue.IDX);
                            rowChild(item, value, row);
                        } else {
                            // Place holder
                            item.children().forEach(shapeItem -> row.addNull());
                        }
                    } else {
                        /*
                         * The condition is for calculation
                         * java.lang.IllegalArgumentException:
                         * Merged region A301 must contain 2 or more cells
                         * Simple with rows
                         */
                        if (1 < max) {
                            /*
                             * Here fix issue
                             * java.lang.IllegalStateException:
                             * Cannot add merged region N306:N308 to sheet because it overlaps with an existing merged region (N304:N306).
                             */
                            row.add(itemRow(each.getValue(column), max));
                        } else {
                            row.add(each.getValue(column));
                        }
                    }
                });
                combined.add(row);
                /*
                 * Repeat
                 */
                for (int idx = 1; idx < max; idx++) {
                    final JsonArray addOn = new JsonArray();
                    final int maxIdx = idx;
                    columns.forEach(column -> {
                        if (metaAtom.isComplex(column)) {
                            final HMetaField item = metaAtom.field(column);
                            final JsonArray columnValue = each.getJsonArray(column);
                            final int valueLength = columnValue.size();
                            if (Ut.isNotNil(columnValue) && 1 < columnValue.size()) {
                                if (maxIdx < valueLength) {
                                    final JsonObject value = columnValue.getJsonObject(maxIdx);
                                    /*
                                     * children field
                                     */
                                    rowChild(item, value, addOn);
                                } else {
                                    item.children().forEach(shapeItem -> addOn.addNull());
                                }
                            } else {
                                // Place holder
                                item.children().forEach(shapeItem -> addOn.addNull());
                            }
                        } else {
                            addOn.addNull();
                        }
                    });
                    combined.add(addOn);
                }
            });
        } else {
            final JsonArray labelHeader = new JsonArray();
            final JsonArray fieldHeader = new JsonArray();
            columns.forEach(column -> {
                labelHeader.add(headers.get(column));
                fieldHeader.add(column);
            });
            combined.add(labelHeader);
            combined.add(fieldHeader);

            /* Data Part */
            Ut.itJArray(data, (each, index) -> {
                final JsonArray row = new JsonArray();
                /* Data Part */
                columns.stream().map(each::getValue).forEach(row::add);

                combined.add(row);
            });
        }
        return Ux.future(combined);
    }

    static Future<JsonArray> combineAsync(final JsonArray data, final ConcurrentMap<String, String> headers) {
        /* Header sequence */
        final List<String> columns = new ArrayList<>(headers.keySet());
        return combineAsync(data, headers, columns, null);
    }

    private static JsonObject itemRow(final Object item, final int row) {
        final JsonObject itemString = new JsonObject();
        itemString.put("rows", row);
        itemString.put("cols", 0);
        itemString.put("value", item);
        return itemString;
    }

    private static JsonObject itemColumn(final Object item, final int column) {
        final JsonObject itemString = new JsonObject();
        itemString.put("cols", column);
        itemString.put("value", item);
        itemString.put("rows", 0);
        return itemString;
    }

    private static void rowChild(final HMetaField item, final JsonObject value, final JsonArray row) {
        /*
         * children field
         */
        item.children().forEach(shapeItem -> {
            if (Objects.isNull(value)) {
                // Place holder
                row.addNull();
            } else {
                // Child value here
                final String childField = shapeItem.name();
                final Object childValue = value.getValue(childField);
                row.add(childValue);
            }
        });
    }

    private static int rowCalculate(final JsonObject json, final Set<String> fieldSet) {
        final Set<Integer> maxSet = new HashSet<>();
        fieldSet.forEach(field -> {
            final Object value = json.getValue(field);
            if (Objects.nonNull(value)) {
                final JsonArray ref;
                if (value instanceof JsonArray) {
                    ref = ((JsonArray) value);
                    maxSet.add(ref.size());
                } else if (value instanceof String) {
                    ref = Ut.toJArray(value.toString());
                    maxSet.add(ref.size());
                } else {
                    ref = new JsonArray();
                    maxSet.add(0);
                }
                // Replaced the data part here
                json.put(field, ref);
            }
        });
        return maxSet.stream().reduce(0, Math::max);
    }
}
