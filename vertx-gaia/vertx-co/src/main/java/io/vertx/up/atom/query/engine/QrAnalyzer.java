package io.vertx.up.atom.query.engine;

import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * ## Critical Query Engine Class
 *
 * ### 1. Intro
 *
 * Provide visitor on json to process json syntax visitor especial in Tree Mode.
 *
 * ### 2. Features
 *
 * > The default operator is `OR` instead of the value `AND`.
 *
 * ```json
 * // <pre><code class="json">
 *     {
 *         "AND Condition": {
 *             "field1": "value1",
 *             "field2": "value2",
 *             "": true
 *         },
 *         "OR Condition": {
 *             "field1": "value1",
 *             "field2": "value2",
 *             "": false
 *         },
 *         "OR Default": {
 *             "field1": "value1",
 *             "field2": "value2"
 *         }
 *     }
 * // </code></pre>
 * ```
 *
 * ### 3. CRUD
 *
 * #### 3.1. Save
 *
 * When user want to `add` new condition in current criteria, there should be situation to limit the original criteria,
 * it means that the connector of this kind situation should be `AND` instead of `OR`, but except the default connector
 * provided.
 *
 * |Situation|Comment|
 * |---|:---|
 * |`{}`|No `field = value` in current input json object.|
 * |`{field1:value1}`|There is one `field = value` in current json object.|
 * |`{field1:value1,field2:value2}`|More than one `field = value` existing and the system should calculate the operator.|
 *
 * Here are additional step to append the same condition in json object here.
 *
 * > ADD, APPEND mode only.
 *
 * #### 3.2. Remove
 *
 * When user want to `remove` current condition, there should be situation of following two:
 *
 * 1. `field,op` as Qr key, match the condition fully.
 * 2. `field` as Qr field, match the condition witch start with `field`.
 *
 * #### 3.3. Update
 *
 * When user want to `update` current condition, the system visit the tree to find the matched `fieldExpr = value` first,
 * when the condition has been found, replace the original condition directly.
 *
 * 1. Replace Mode: Call `=` to replace current condition.
 * 2. Append Mode: When `i` operator, combine the condition value to `field,i = []` based on operator.
 *
 * > REPLACE mode only.
 *
 * #### 3.4. Transfer
 *
 * Input is `field2 = value2` to replace `field1 = value1` condition instead of update/save/delete.
 *
 * In this kind of situation, there may be different `field1` converted to same `field2`, then the `field2` should
 * be merged ( APPEND ) to original one.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class QrAnalyzer implements QrDo {
    private final JsonObject raw = new JsonObject();

    QrAnalyzer(final JsonObject input) {
        this.raw.mergeIn(input, true);
    }

    /**
     * Check whether json object is complex
     *
     * 1. When any one value is `JsonObject`, it's true.
     * 2. otherwise the result is false.
     *
     * @param source {@link io.vertx.core.json.JsonObject} input json
     *
     * @return {@link java.lang.Boolean}
     */
    static boolean isComplex(final JsonObject source) {
        return source.fieldNames().stream()
            .anyMatch(field -> isJson(source.getValue(field)));
    }

    /**
     * Return true if the value type is {@link io.vertx.core.json.JsonObject}
     *
     * @param value {@link java.lang.Object} Input value that will be checked.
     *
     * @return {@link java.lang.Boolean}
     */
    private static boolean isJson(final Object value) {
        if (Objects.isNull(value)) {
            /* null pointer */
            return false;
        } else {
            /* valid */
            return Ut.isJObject(value.getClass());
        }
    }

    /**
     * @param fieldExpr {@link java.lang.String}
     * @param value     {@link java.lang.Object}
     *
     * @return {@link QrDo}
     */
    @Override
    public QrDo save(final String fieldExpr, final Object value) {
        if (VString.EMPTY.equals(fieldExpr)) {
            /*
             * "" for AND / OR
             */
            this.raw.put(fieldExpr, value);
        } else {
            final QrItem item = new QrItem(fieldExpr).value(value);
            /*
             * Boolean for internal execution
             */
            final AtomicBoolean existing = new AtomicBoolean();
            existing.set(Boolean.FALSE);
            this.itExist(this.raw, item, (qr, ref) -> {
                /*
                 * Flat processing.
                 */
                existing.set(Boolean.TRUE);
                /*
                 * Save Operation
                 */
                this.saveWhere(ref, item, qr);
            });
            if (!existing.get()) {
                /*
                 * Add Operation.
                 */
                this.addWhere(this.raw, item);
            }
        }
        return this;
    }

    /**
     * @param fieldExpr {@link java.lang.String} Removed fieldExpr
     * @param fully     {@link java.lang.Boolean} Removed fully or ?
     *
     * @return {@link QrDo}
     */
    @Override
    public QrDo remove(final String fieldExpr, final boolean fully) {
        /*
         * Existing the QrItem
         */
        final QrItem item = new QrItem(fieldExpr);
        this.itExist(this.raw, (field, value) -> {
            if (fully) {
                /* Compare fieldExpr */
                return field.equals(item.qrKey());
            } else {
                /* Check whether each start with field. */
                return field.startsWith(item.field());
            }
            // Remove matched condition.
        }, (qr, ref) -> ref.remove(qr.qrKey()));
        return this;
    }

    @Override
    public void match(final String field, final BiConsumer<QrItem, JsonObject> consumer) {
        this.itExist(this.raw, (fieldExpr, value) -> fieldExpr.startsWith(field), consumer);
    }

    /**
     * 1. REPLACE
     *
     * @param fieldExpr {@link java.lang.String}
     * @param newValue  {@link java.lang.Object}
     *
     * @return {@link QrDo}
     */
    @Override
    public QrDo update(final String fieldExpr, final Object newValue) {
        /*
         * Existing the QrItem
         */
        final QrItem item = new QrItem(fieldExpr).value(newValue);
        this.itExist(this.raw, item, (qr, ref) -> ref.put(qr.qrKey(), item.value()));
        return this;
    }

    @Override
    public JsonObject toJson() {
        return this.raw;
    }

    /**
     * Combine two `QrItem` based on different operator`, current version
     *
     * 1. = combine
     * 2. in combine
     *
     * Other operator could not be support
     *
     * @param raw     {@link io.vertx.core.json.JsonObject} The input json object
     * @param newItem {@link QrItem} the new added item to current object.
     * @param oldItem {@link QrItem} the original added item to current object.
     */
    @SuppressWarnings("all")
    private void saveWhere(final JsonObject raw, final QrItem newItem, final QrItem oldItem) {
        if (newItem.valueEq(oldItem)) {
            /* value equal, no change detect, skip */
            return;
        }
        /*
         * Here the qrKey is the same between newItem and oldItem
         */
        final Boolean isAnd = raw.getBoolean(VString.EMPTY, Boolean.FALSE);
        if (Qr.Op.EQ.equals(newItem.op())) {        // =
            // field,= or field
            raw.remove(newItem.qrKey());
            raw.remove(newItem.field());

            final JsonArray in = new JsonArray();
            // A = x OR A = y -> A in [x,y]
            // A = x AND A = y -> A in []
            if (!isAnd) {
                in.add(newItem.value());
                in.add(oldItem.value());
            }
            raw.put(newItem.field() + ",i", in);
        } else if (Qr.Op.IN.equals(newItem.op())) {
            raw.put(newItem.qrKey(), QrDo.combine(newItem.value(), oldItem.value(), isAnd));
        }
    }

    /**
     * Add new `QrItem` to current json criteria object.
     *
     * @param raw  {@link io.vertx.core.json.JsonObject} The input json object
     * @param item {@link QrItem} the new added item to current object.
     */
    private void addWhere(final JsonObject raw, final QrItem item) {
        if (Ut.isNil(raw)) {
            /*
             * Empty add new key directly here, because there is no condition,
             * in this kind of situation, the system will add `qrKey = value` to current
             * json object as the unique condition.
             */
            raw.put(item.qrKey(), item.value());
        } else {
            if (VValue.ONE == raw.size()) {
                /*
                 * If raw size = 1, add the condition to let the size to be 3.
                 * Here the connector will be `AND` auto.
                 */
                raw.put(VString.EMPTY, Boolean.TRUE);
                raw.put(item.qrKey(), item.value());
            } else {
                /*
                 * The raw size > 1, check connector
                 * 1. If And, add directly.
                 * 2. If Or, Convert the whole or condition to get the new one.
                 */
                final Boolean isAnd = raw.getBoolean(VString.EMPTY, Boolean.FALSE);
                if (isAnd) {
                    raw.put(item.qrKey(), item.value());
                } else {
                    /*
                     * Or connector
                     */
                    final JsonObject replaced = new JsonObject();
                    replaced.put(VString.EMPTY, Boolean.TRUE);
                    replaced.put(item.qrKey(), item.value());
                    replaced.put("$0", raw.copy());
                    /*
                     * Current raw will be replaced by new object.
                     */
                    raw.clear();
                    raw.mergeIn(replaced, true);
                }
            }
        }
    }

    private void itExist(final JsonObject source, final QrItem item,
                         final BiConsumer<QrItem, JsonObject> consumer) {
        this.itExist(source, (field, value) -> field.equals(item.qrKey()), consumer);
    }

    private void itExist(final JsonObject source,
                         final BiPredicate<String, Object> predicate,
                         final BiConsumer<QrItem, JsonObject> consumer) {
        if (isComplex(source)) {
            /* Complex criteria ( Tree Mode ) */
            source.copy().fieldNames().stream()
                .filter(field -> isJson(source.getValue(field)))
                .forEach(field -> {
                    final JsonObject itemJson = source.getJsonObject(field);
                    this.itExist(itemJson, predicate, consumer);
                    /*
                     * If the linear operation happened.
                     * Empty object processing
                     * Remove `{}` condition instead of other here.
                     * */
                    if (Ut.isNil(itemJson)) {
                        source.remove(field);
                    }
                });

        }
        this.itLinear(source, predicate, consumer);
    }

    private void itLinear(final JsonObject source,
                          final BiPredicate<String, Object> predicate,
                          final BiConsumer<QrItem, JsonObject> consumer) {
        /* Simple criteria ( Linear Mode ) */
        source.copy().fieldNames().stream()
            /* Non complex json */
            .filter(field -> !isJson(source.getValue(field)))
            .filter(field -> {
                // Predicate for `field = Object`
                final Object value = source.getValue(field);
                return predicate.test(field, value);
            })
            .forEach(field -> {
                // TiConsumer
                final Object value = source.getValue(field);
                final QrItem item = new QrItem(field).value(value);
                consumer.accept(item, source);
            });
        /*
         * Post operation: When only one key existing: `"" = xx`, remove it.
         *
         * Another situation is that such as:
         *
         * ```json
         * {
         *      "": true,
         *      "field1": "xxx"
         * }
         * ```
         *
         * This situation the `""` could not be removed because it will be calculated
         * in future ADD / APPEND etc.
         */
        if (VValue.ONE == source.size() && source.containsKey(VString.EMPTY)) {
            /* Removed single "" condition */
            source.remove(VString.EMPTY);
        }
    }
}
