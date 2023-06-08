package io.vertx.up.commune;

import io.modello.specification.HRecord;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.exchange.BTree;
import io.vertx.up.eon.KWeb;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

class ActJArray extends ActMapping implements Serializable {

    private final transient JsonArray data = new JsonArray();
    private final transient JsonObject header;

    ActJArray(final Envelop envelop) {
        /* Header Init */
        this.header = envelop.headersX();
        /* Data Init */
        this.partData(envelop);
    }

    private void partData(final Envelop envelop) {
        final JsonObject rawJson = envelop.data();
        if (!Ut.isNil(rawJson)) {
            final long counter = rawJson.fieldNames().stream()
                .filter(KWeb.MULTI.INDEXES::containsValue)
                .count();
            final JsonArray body;
            if (0 < counter) {
                /*
                 * Interface style
                 * {
                 *      "0": "xxx",
                 *      "1": {
                 *          "name": "x",
                 *          "name1": "y"
                 *      }
                 * }
                 */
                final JsonArray found = rawJson.fieldNames().stream()
                    .filter(Objects::nonNull)
                    .map(rawJson::getValue)
                    /*
                     * Predicate to test whether value is JsonArray
                     * If JsonObject, then find the first JsonArray as body
                     */
                    .filter(value -> value instanceof JsonArray)
                    .map(item -> (JsonArray) item)
                    .findFirst().orElse(null);

                /* Copy new data structure */
                body = null == found ? new JsonArray() : found.copy();
                this.data.addAll(body);
            } else {
                /*
                 * rawJson could not be JsonArray
                 */
                JsonArray inputData = new JsonArray();
                if (rawJson.containsKey(KWeb.ARGS.PARAM_BODY)) {
                    /*
                     * Common Style
                     * {
                     *      "field": "value",
                     *      "$$__BODY__$$": "body"
                     * }
                     */
                    inputData = rawJson.getJsonArray(KWeb.ARGS.PARAM_BODY);
                    if (Objects.nonNull(inputData)) {
                        /*
                         * Copy the json array data and it will be passed
                         */
                        inputData = inputData.copy();
                    }
                }
                /*
                 * merged headers
                 */
                inputData.stream().filter(item -> item instanceof JsonObject)
                    .map(item -> (JsonObject) item)
                    .forEach(item -> item.mergeIn(this.header.copy(), true));
                this.data.addAll(inputData);
            }
        }
    }

    HRecord[] getRecords(final HRecord definition, final BTree mapping) {
        /* Record Init */
        final int size = this.data.size();
        final HRecord[] records = new HRecord[size];
        for (int idx = 0; idx < size; idx++) {
            /*
             * 两种格式
             * 1）String：全主键
             * 2）JsonArray：有数据
             */
            final Object input = this.data.getValue(idx);
            records[idx] = this.getRecord(input, definition, mapping);
        }
        return records;
    }

    JsonArray getJson(final BTree mapping) {
        if (this.isBefore(mapping)) {
            final JsonArray normalized = new JsonArray();
            Ut.itJArray(this.data)
                .map(item -> this.mapper().in(item, mapping.child()))
                .forEach(normalized::add);
            return normalized;
        } else {
            return this.data;
        }
    }
}
