package io.vertx.up.commune;

import io.horizon.uca.qr.syntax.Ir;
import io.modello.specification.HRecord;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.exchange.BTree;
import io.vertx.up.eon.KWeb;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/*
 * Business Request
 * JsonObject
 */
class ActJObject extends ActMapping implements Serializable {

    /* Raw data of `Envelop` object/reference */
    private final transient JsonObject data = new JsonObject();
    private final transient JsonObject query = new JsonObject();

    ActJObject(final Envelop envelop) {
        /* Header Init */
        final JsonObject header = envelop.headersX();
        this.data.mergeIn(header, true);

        /* Data Init */
        this.partData(envelop);
    }

    private void partData(final Envelop envelop) {
        final JsonObject rawJson = envelop.data();
        if (!Ut.isNil(rawJson)) {
            final long counter = rawJson.fieldNames().stream()
                .filter(KWeb.MULTI.INDEXES::containsValue)
                .count();
            final JsonObject body;
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
                final JsonObject found = rawJson.fieldNames().stream()
                    .filter(Objects::nonNull)
                    .map(rawJson::getValue)
                    /*
                     * Predicate to test whether value is JsonObject
                     * If JsonObject, then find the first JsonObject as body
                     */
                    .filter(value -> value instanceof JsonObject)
                    .map(item -> (JsonObject) item)
                    .findFirst().orElse(null);

                /* Copy new data structure */
                body = null == found ? new JsonObject() : found.copy();
            } else {

                body = rawJson.copy();
                /*
                 * Cross reference
                 */
                JsonObject cross = new JsonObject();
                if (body.containsKey(KWeb.ARGS.PARAM_BODY)) {
                    /*
                     * Common style
                     * {
                     *      "field": "value",
                     *      "$$__BODY__$$": "body"
                     * }
                     */
                    final JsonObject inputData = body.copy();
                    body.fieldNames().stream()
                        .filter(field -> !KWeb.ARGS.PARAM_BODY.equals(field))
                        /*
                         * NON, $$__BODY__$$
                         */
                        .forEach(field -> this.data.put(field, inputData.getValue(field)));
                    final Object bodyData = body.getValue(KWeb.ARGS.PARAM_BODY);
                    if (bodyData instanceof JsonObject) {
                        cross = (JsonObject) bodyData;
                    } else {
                        cross = new JsonObject();
                    }
                }
                /*
                 * $$__BODY__$$ is null
                 * */
                if (!Ut.isNil(cross)) {
                    body.clear();
                    /*
                     * Modify to latest body
                     */
                    body.mergeIn(cross, true);
                }
            }
            /*
             * isQuery ? criteria
             * Until now the system has calculated the body data here, the condition should be enhancement
             */
            if (body.containsKey(Ir.KEY_CRITERIA) || body.containsKey(Ir.KEY_PROJECTION)) {
                /*
                 * JqTool part
                 */
                Arrays.stream(Ir.KEY_QUERY).filter(field -> Objects.nonNull(body.getValue(field)))
                    .forEach(field -> {
                        this.query.put(field, body.getValue(field));
                        /*
                         * The criteria parameters does't occurs in future body here.
                         * {
                         *     pager,
                         *     sorter,
                         *     projection,
                         *     criteria
                         * }
                         */
                        body.remove(field);
                    });
            }
            // fill criteria field when query is not empty
            if (Ut.isNotNil(this.query) && !this.query.containsKey(Ir.KEY_CRITERIA)) {
                this.query.put(Ir.KEY_CRITERIA, new JsonObject());
            }
            if (Ut.isNotNil(body)) {
                /*
                 * Common data
                 */
                this.data.mergeIn(body.copy(), true);
            }
        }
    }

    public JsonObject getQuery() {
        return this.query;
    }

    /*
     * JsonObject -> Record
     */
    HRecord getRecord(final HRecord definition, final BTree mapping) {
        return this.getRecord(this.data, definition, mapping);
    }

    JsonObject getJson(final BTree mapping) {
        if (this.isBefore(mapping)) {
            return this.mapper().in(this.data, mapping.child());
        } else {
            return this.data;
        }
    }
}
