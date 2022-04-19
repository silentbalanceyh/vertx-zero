package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.MetaInstance;
import io.vertx.tp.workflow.atom.WMove;
import io.vertx.tp.workflow.atom.WMoveRule;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractTransfer implements Behaviour {
    protected final transient JsonObject config = new JsonObject();
    private final transient ConcurrentMap<String, WMove> moveMap = new ConcurrentHashMap<>();

    @Override
    public Behaviour bind(final JsonObject config) {
        final JsonObject sure = Ut.valueJObject(config);
        this.config.mergeIn(sure.copy(), true);
        this.config.fieldNames().stream()
            // Ignore `record` and `todo` configuration key
            .filter(field -> !KName.RECORD.equals(field))
            .filter(field -> !KName.Flow.TODO.equals(field))
            .filter(field -> !KName.LINKAGE.equals(field))
            .forEach(field -> {
                final JsonObject value = this.config.getJsonObject(field);
                final WMove item = WMove.create(field, value);
                this.moveMap.put(field, item);
            });
        return this;
    }

    @Override
    public Behaviour bind(final MetaInstance metadata) {
        // Empty Binding on Instance
        return this;
    }


    protected WMove moveGet(final String node) {
        return this.moveMap.getOrDefault(node, WMove.empty());
    }

    protected ConcurrentMap<String, WMove> rules() {
        return this.moveMap;
    }

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
        if (!params.containsKey(KName.KEY)) {
            /*
             * Add `key` field to root json object
             * Todo Key
             */
            params.put(KName.KEY, UUID.randomUUID().toString());
        }


        if (!params.containsKey(KName.Flow.TRACE_KEY)) {
            /*
             * Add `traceKey` field to root json object
             */
            params.put(KName.Flow.TRACE_KEY, UUID.randomUUID().toString());
        }
        return Ux.future(this.recordInput(params));
    }

    protected JsonObject recordInput(final JsonObject params) {
        final Object record = params.getValue(KName.RECORD);
        if (Objects.nonNull(record)) {
            if (record instanceof JsonObject) {
                // Record is JsonObject
                final JsonObject recordJ = (JsonObject) record;
                this.inputAsync(params, recordJ, true);
            } else if (record instanceof JsonArray) {
                // Record is JsonArray ( Each Json )
                final JsonArray recordA = (JsonArray) record;
                final JsonArray modelChild = new JsonArray();
                Ut.itJArray(recordA)
                    .map(json -> this.inputAsync(params, json, false))
                    .forEach(modelChild::add);
                params.put(KName.MODEL_CHILD, modelChild.encode());         // String Format for `modelChild`
            }
        }
        return params;
    }

    protected JsonObject recordMove(final JsonObject request, final WMoveRule rule) {
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

    private String inputAsync(final JsonObject params, final JsonObject record, final boolean o2o) {
        /*
         * Here the params JsonObject instance must contain `key` field
         */
        final String recordKey;
        if (record.containsKey(KName.KEY)) {
            /*
             * Get existing `key` from record json object
             */
            recordKey = record.getString(KName.KEY);
        } else {
            /*
             * Generate new `key` here
             */
            recordKey = UUID.randomUUID().toString();
            record.put(KName.KEY, recordKey);
        }


        /*
         * Copy the `key` of record to ticket when
         * JsonObject <-> JsonObject
         */
        if (o2o) {
            params.put(KName.MODEL_KEY, recordKey);
        }


        /*
         * Copy the `key` of ticket to each record
         */
        record.put(KName.MODEL_KEY, params.getValue(KName.Flow.TRACE_KEY));
        return recordKey;
    }
}
