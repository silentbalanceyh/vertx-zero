package io.vertx.mod.workflow.uca.central;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.runtime.WTransition;
import io.vertx.mod.workflow.uca.toolkit.URequest;
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
    protected Future<JsonObject> inputAsync(final JsonObject params, final WTransition wTransition) {
        return wTransition.start().compose(started -> {
            // Prepare for Todo
            final JsonObject inputJ = URequest.inputJ(params);
            // Prepare for Record
            return Ux.future(this.recordInput(inputJ));
        });
    }

    protected JsonObject recordInput(final JsonObject requestJ) {
        final Object record = requestJ.getValue(KName.RECORD);
        if (Objects.nonNull(record)) {
            if (record instanceof JsonObject) {
                // Record is JsonObject
                final JsonObject recordJ = (JsonObject) record;
                URequest.inputJ(requestJ, recordJ, true);
            } else if (record instanceof JsonArray) {
                // Record is JsonArray ( Each Json )
                final JsonArray recordA = (JsonArray) record;
                final JsonArray modelChild = new JsonArray();
                Ut.itJArray(recordA)
                    .map(json -> URequest.inputJ(requestJ, json, false))
                    .forEach(modelChild::add);
                requestJ.put(KName.MODEL_CHILD, modelChild.encode());         // String Format for `modelChild`
            }
        }
        return requestJ;
    }
}
