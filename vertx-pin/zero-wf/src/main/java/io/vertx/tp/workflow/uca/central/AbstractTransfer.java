package io.vertx.tp.workflow.uca.central;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.runtime.WRule;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractTransfer extends BehaviourStandard {
    /*
     * Basic Data Structure Here:
     * {
     *      "key": "WTodo Key",
     *      "traceKey": "WTicket Key",
     *      "record": {
     *          "key": "Entity / Extension Ticket Key",
     *          "modelKey": "Refer WTicket Key"
     *      }
     * }
     * Here the request could be the spec situation
     * -- record is null ( No Related Here )
     */
    protected Future<JsonObject> inputAsync(final JsonObject params) {
        final JsonObject inputJ = AidData.inputJ(params);
        return Ux.future(this.recordInput(inputJ));
    }

    protected JsonObject recordInput(final JsonObject params) {
        final Object record = params.getValue(KName.RECORD);
        if (Objects.nonNull(record)) {
            if (record instanceof JsonObject) {
                // Record is JsonObject
                final JsonObject recordJ = (JsonObject) record;
                AidData.inputJ(params, recordJ, true);
            } else if (record instanceof JsonArray) {
                // Record is JsonArray ( Each Json )
                final JsonArray recordA = (JsonArray) record;
                final JsonArray modelChild = new JsonArray();
                Ut.itJArray(recordA)
                    .map(json -> AidData.inputJ(params, json, false))
                    .forEach(modelChild::add);
                params.put(KName.MODEL_CHILD, modelChild.encode());         // String Format for `modelChild`
            }
        }
        return params;
    }

    protected JsonObject recordMove(final JsonObject request, final WRule rule) {
        final Object recordData = request.getValue(KName.RECORD);
        if (Objects.isNull(recordData)) {
            return request;
        }
        if (recordData instanceof JsonObject) {
            final JsonObject recordJ = ((JsonObject) recordData);
            recordJ.mergeIn(rule.getRecord());
            request.put(KName.RECORD, recordJ);
        } else if (recordData instanceof JsonArray) {
            final JsonArray recordA = ((JsonArray) recordData);
            Ut.itJArray(recordA).forEach(recordJ -> recordJ.mergeIn(rule.getRecord()));
            request.put(KName.RECORD, recordA);
        }
        return request;
    }
}
