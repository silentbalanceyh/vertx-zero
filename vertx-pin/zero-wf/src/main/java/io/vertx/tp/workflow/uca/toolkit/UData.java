package io.vertx.tp.workflow.uca.toolkit;

import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.runtime.WTransition;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.task.Task;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class UData {

    // ---------------------- Input Data --------------------------------
    public static JsonObject inputJ(final JsonObject params) {
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
        return params;
    }

    public static String inputJ(final JsonObject params,
                                final JsonObject record,
                                final boolean isObject) {
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
        if (isObject) {
            params.put(KName.MODEL_KEY, recordKey);
        }


        /*
         * Copy the `key` of ticket to each record
         */
        record.put(KName.MODEL_KEY, params.getValue(KName.Flow.TRACE_KEY));
        return recordKey;
    }


    // ---------------------- Close Data --------------------------------
    /*
     * Close current
     * {
     *      "status": "FINISHED",
     *      "finishedAt": "",
     *      "finishedBy": "",
     *      "flowEnd": "Depend on instance",
     *      "active": true
     * }
     */
    public static JsonObject closeJ(final JsonObject params, final WTransition wTransition) {
        final JsonObject updatedData = params.copy();
        updatedData.put(KName.STATUS, TodoStatus.FINISHED.name());
        final String user = params.getString(KName.UPDATED_BY);
        updatedData.put(KName.Auditor.FINISHED_AT, Instant.now());
        updatedData.put(KName.Auditor.FINISHED_BY, user);
        // updatedAt / updatedBy contain values
        updatedData.put(KName.ACTIVE, Boolean.TRUE);


        if (wTransition.isEnded()) {
            /*
             * Closable Data, following three fields must be happened the same
             * - flowEnd = true
             * - closeBy = has value
             * - closeAt = current date
             *
             * Because there is only one close by information
             */
            updatedData.put(KName.Flow.FLOW_END, Boolean.TRUE);
            updatedData.put(KName.Auditor.CLOSE_AT, Instant.now());
            updatedData.put(KName.Auditor.CLOSE_BY, user);
        }
        return updatedData;
    }

    public static JsonObject cancelJ(final JsonObject params, final WTransition wTransition, final Set<String> historySet) {
        return endJ(params, wTransition, historySet, TodoStatus.CANCELED);
    }

    public static JsonObject closeJ(final JsonObject params, final WTransition wTransition, final Set<String> historySet) {
        return endJ(params, wTransition, historySet, TodoStatus.FINISHED);
    }

    // ------------------ Private Method -------------------

    private static JsonObject endJ(final JsonObject params, final WTransition wTransition,
                                   final Set<String> historySet, final TodoStatus status) {
        final JsonObject todoData = params.copy();
        final String user = todoData.getString(KName.UPDATED_BY);
        /*
         * History building
         */
        final Set<String> traceSet = new HashSet<>(historySet);
        final Task task = wTransition.from();
        if (Objects.nonNull(task)) {
            traceSet.add(task.getTaskDefinitionKey());
        }
        /*
         * Todo Processing（Record Keep）
         */
        {
            final JsonObject history = new JsonObject();
            history.put(KName.HISTORY, Ut.toJArray(traceSet));
            // todoData.put(KName.Flow.TRACE_EXTRA, history.encode());
            todoData.put(KName.STATUS, status.name());
            todoData.put(KName.Auditor.FINISHED_AT, Instant.now());
            todoData.put(KName.Auditor.FINISHED_BY, user);
            todoData.put(KName.Flow.FLOW_END, Boolean.TRUE);
        }
        /*
         * Closable Data
         */
        {
            // Cancel ticket will ignore closeBy
            if (TodoStatus.CANCELED == status) {
                todoData.put(KName.Auditor.CANCEL_AT, Instant.now());
                todoData.put(KName.Auditor.CANCEL_BY, user);
            }
            // Close ticket will ignore cancelBy
            if (TodoStatus.FINISHED == status) {
                todoData.put(KName.Auditor.CLOSE_AT, Instant.now());
                todoData.put(KName.Auditor.CLOSE_BY, user);
            }
        }
        return todoData;
    }
}
