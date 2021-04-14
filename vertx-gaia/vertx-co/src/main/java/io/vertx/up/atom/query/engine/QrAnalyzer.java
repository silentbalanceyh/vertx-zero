package io.vertx.up.atom.query.engine;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.util.Ut;

import java.util.Objects;
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
    private final transient JsonObject raw = new JsonObject();

    QrAnalyzer(final JsonObject input) {
        this.raw.mergeIn(input, true);
    }

    @Override
    public QrDo save(final String fieldExpr, final Object value) {
        final QrItem item = new QrItem(fieldExpr).value(value);
        if (Ut.isNil(this.raw)) {
            /*
             * Empty add new key directly here.
             */
            this.raw.put(item.qrKey(), item.value());
        } else {

        }
        return this;
    }

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
    public JsonObject toJson() {
        return this.raw;
    }

    /**
     * 1. REPLACE
     *
     * @param source {@link io.vertx.core.json.JsonObject} The criteria source
     * @param item   {@link QrItem} The item that will be added / saved
     */
    static void update(final JsonObject source, final QrItem item) {

    }

    /**
     * 1. DELETE original condition
     * 2. APPEND current condition
     *
     * @param source {@link io.vertx.core.json.JsonObject} The criteria source
     * @param from   {@link QrItem} The item that will be removed.
     * @param to     {@link QrItem} The item that will be append.
     */
    static void transfer(final JsonObject source, final QrItem from, final QrItem to) {

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
        if (Values.ONE == source.size() && source.containsKey(Strings.EMPTY)) {
            /* Removed single "" condition */
            source.remove(Strings.EMPTY);
        }
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
}
