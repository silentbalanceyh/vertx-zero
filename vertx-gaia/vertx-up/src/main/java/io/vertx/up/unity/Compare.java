package io.vertx.up.unity;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Record;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;

/*
 * Compare two entity collection for
 * 1) Get `ADD` operation
 * 2) Get `UPDATE` operation
 * 3) Get `DELETE` operation
 */
class Compare {
    /*
     * The uniqueSet contains all unique fields
     */
    static <T> ConcurrentMap<ChangeFlag, List<T>> compare(final List<T> original, final List<T> current,
                                                          final Set<String> uniqueSet,
                                                          final String pojoFile) {
        return compare(original, current, (entity) -> {
            /*
             * The fnValue should calculate unique value subset
             */
            final JsonObject uniqueValue = new JsonObject();
            uniqueSet.forEach(field -> {
                final Object fieldValue = Ut.field(entity, field);
                uniqueValue.put(field, fieldValue);
            });
            return uniqueValue;
        }, pojoFile);
    }

    static <T, R> ConcurrentMap<ChangeFlag, List<T>> compare(final List<T> original, final List<T> current,
                                                             final Function<T, R> fnValue,
                                                             final String pojoFile) {
        final ConcurrentMap<ChangeFlag, List<T>> comparedMap = new ConcurrentHashMap<ChangeFlag, List<T>>() {
            {
                this.put(ChangeFlag.DELETE, new ArrayList<>());
                this.put(ChangeFlag.UPDATE, new ArrayList<>());
                this.put(ChangeFlag.ADD, new ArrayList<>());
            }
        };
        if (Objects.isNull(original) || original.isEmpty()) {
            /*
             * No `DELETE`, No `UPDATE`
             * In this situation, all the entity should be `ADD`
             * Not null for double checking
             */
            if (Objects.nonNull(current)) {
                comparedMap.get(ChangeFlag.ADD).addAll(current);
            }
        } else {
            /*
             * Calculation for `DELETE`
             */
            original.forEach(originalItem -> {
                final T latestItem = find(current, originalItem, fnValue);
                if (Objects.isNull(latestItem)) {
                    /*
                     * Delete
                     */
                    comparedMap.get(ChangeFlag.DELETE).add(originalItem);
                }
            });
            /*
             * Calculation for `ADD / UPDATE`
             */
            current.forEach(latestItem -> {
                final T previous = find(original, latestItem, fnValue);
                if (Objects.isNull(previous)) {
                    /*
                     * ADD
                     */
                    comparedMap.get(ChangeFlag.ADD).add(latestItem);
                } else {
                    /*
                     * original / current contains
                     * UPDATE
                     */
                    final T updated = combine(previous, latestItem, pojoFile);
                    comparedMap.get(ChangeFlag.UPDATE).add(updated);
                }
            });
        }
        return comparedMap;
    }

    @SuppressWarnings("all")
    private static <T> T combine(final T old, final T latest, final String pojo) {
        if (Objects.isNull(old) && Objects.isNull(latest)) {
            return null;
        } else {
            if (Objects.isNull(old)) {
                return latest;
            } else if (Objects.isNull(latest)) {
                return old;
            } else {
                /*
                 * Convert old entity to json
                 */
                final JsonObject combineJson = Ut.valueJObject(To.toJObject(old, pojo));
                /*
                 * Convert current entity to json
                 */
                final JsonObject latestJson = Ut.valueJObject(To.toJObject(latest, pojo));
                if (latestJson.containsKey("key")) {
                    /*
                     * Because here it will combine previous/current json object
                     * The system should remove latest `key` field ( Primary Key Removed )
                     */
                    latestJson.remove("key");
                }
                /*
                 * Merged
                 */
                combineJson.mergeIn(latestJson, true);
                /*
                 * Deserialization
                 */
                final Class<?> clazz = latest.getClass();
                return (T) From.fromJson(combineJson, clazz, pojo);
            }
        }
    }

    private static <T, R> T find(final List<T> list, final T current, final Function<T, R> fnValue) {
        if (Objects.isNull(list) || list.isEmpty() || Objects.isNull(current)) {
            /*
             * Could not be found here
             * 1) list is null          ( Source List )
             * 2) list is empty         ( Source List )
             * 3) current is null       ( Target Entity )
             */
            return null;
        } else {
            final R comparedValue = fnValue.apply(current);
            if (Objects.isNull(comparedValue)) {
                /*
                 * Compared value is null, return null instead of deeply lookup
                 */
                return null;
            } else {
                return list.stream().filter(Objects::nonNull)
                    .filter(each -> comparedValue.equals(fnValue.apply(each)))
                    .findAny().orElse(null);
            }
        }
    }

    @SuppressWarnings("all")
    static <T> T updateT(final T query, final JsonObject params) {
        Objects.requireNonNull(query);
        final Class<?> entityCls = query.getClass();
        final JsonObject original = To.toJObject(query, "");
        original.mergeIn(params, true);
        return (T) From.fromJson(original, entityCls, "");
    }

    @SuppressWarnings("all")
    static <T> T cloneT(final T input) {
        if (Objects.isNull(input)) {
            return null;
        }
        final Class<?> clazz = input.getClass();
        final JsonObject original = To.toJObject(input, "");
        return (T) From.fromJson(original, clazz, "");
    }

    static <ID> Record updateR(final Record record, final JsonObject data,
                               final Supplier<ID> supplier) {
        Objects.requireNonNull(record);
        record.set(data);
        final ID key = record.key();
        if (Objects.isNull(key)) {
            record.key(supplier.get());
        }
        return record;
    }

    static List<Record> updateR(final List<Record> recordList, final JsonArray array, final String field) {
        final ConcurrentMap<String, JsonObject> dataMap = Ut.elementMap(array, field);
        recordList.forEach(record -> {
            final String key = record.key();
            if (Objects.nonNull(key)) {
                final JsonObject dataJ = dataMap.getOrDefault(key, new JsonObject());
                if (Ut.notNil(dataJ)) {
                    dataJ.remove(field);
                    record.set(dataJ);
                }
            }
        });
        return recordList;
    }

    static <T> List<T> updateT(final List<T> query, final JsonArray params, final String field) {
        Objects.requireNonNull(query);
        if (query.isEmpty()) {
            return new ArrayList<>();
        } else {
            final ConcurrentMap<String, JsonObject> dataMap = Ut.elementMap(params, field);
            final List<T> result = new ArrayList<>();
            query.forEach(item -> {
                final Object key = Ut.field(item, field);
                if (Objects.nonNull(key)) {
                    final JsonObject merge = dataMap.get(key.toString());
                    final T entity = updateT(item, merge);
                    result.add(entity);
                }
            });
            return result;
        }
    }

    static JsonArray updateJ(final JsonArray query, final JsonArray params, final String field) {
        Objects.requireNonNull(query);
        if (Ut.isNil(query)) {
            return new JsonArray();
        } else {
            final ConcurrentMap<String, JsonObject> dataMap = Ut.elementMap(params, field);
            final JsonArray normalized = query.copy();
            Ut.itJArray(normalized).forEach(json -> {
                final String value = json.getString(field);
                if (Objects.nonNull(value)) {
                    final JsonObject merge = dataMap.get(value);
                    if (Objects.nonNull(merge)) {
                        json.mergeIn(merge, true);
                    }
                }
            });
            return normalized;
        }
    }


    static <T> Future<JsonArray> run(final ConcurrentMap<ChangeFlag, List<T>> compared,
                                     final Function<List<T>, Future<List<T>>> insertAsyncFn,
                                     final Function<List<T>, Future<List<T>>> updateAsyncFn) {
        final List<Future<JsonArray>> futures = new ArrayList<>();
        final List<T> qAdd = compared.getOrDefault(ChangeFlag.ADD, new ArrayList<>());
        if (!qAdd.isEmpty()) {
            futures.add(insertAsyncFn.apply(qAdd).compose(Ux::futureA));
        }
        final List<T> qUpdate = compared.getOrDefault(ChangeFlag.UPDATE, new ArrayList<>());
        if (!qUpdate.isEmpty()) {
            futures.add(updateAsyncFn.apply(qUpdate).compose(Ux::futureA));
        }
        return Fn.arrangeA(futures);
    }
}
