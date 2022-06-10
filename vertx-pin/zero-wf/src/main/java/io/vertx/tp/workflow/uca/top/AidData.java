package io.vertx.tp.workflow.uca.top;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WMoveRule;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.task.Task;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AidData {

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
    public static JsonObject closeJ(final JsonObject params, final WProcess wProcess) {
        final JsonObject updatedData = params.copy();
        updatedData.put(KName.STATUS, TodoStatus.FINISHED.name());
        final String user = params.getString(KName.UPDATED_BY);
        updatedData.put(KName.Flow.Auditor.FINISHED_AT, Instant.now());
        updatedData.put(KName.Flow.Auditor.FINISHED_BY, user);
        // updatedAt / updatedBy contain values
        updatedData.put(KName.ACTIVE, Boolean.TRUE);


        if (wProcess.isEnd()) {
            /*
             * Closable Data, following three fields must be happened the same
             * - flowEnd = true
             * - closeBy = has value
             * - closeAt = current date
             *
             * Because there is only one close by information
             */
            updatedData.put(KName.Flow.FLOW_END, Boolean.TRUE);
            updatedData.put(KName.Flow.Auditor.CLOSE_AT, Instant.now());
            updatedData.put(KName.Flow.Auditor.CLOSE_BY, user);
        }
        // Todo based on previous
        final WMoveRule rule = wProcess.ruleFind();
        if (Objects.nonNull(rule) && Ut.notNil(rule.getTodo())) {
            final JsonObject parsed = parseValue(rule.getTodo(), params);
            updatedData.mergeIn(parsed);
        }
        return updatedData;
    }

    public static JsonObject cancelJ(final JsonObject params, final WProcess wProcess, final Set<String> historySet) {
        return endJ(params, wProcess, historySet, TodoStatus.CANCELED);
    }

    public static JsonObject closeJ(final JsonObject params, final WProcess wProcess, final Set<String> historySet) {
        return endJ(params, wProcess, historySet, TodoStatus.FINISHED);
    }


    /*
     * Next
     * {
     *      "key": "New generation",
     *      "serial": "Re-calculation",
     *      "code": "synced with serial",
     *      "traceOrder": "Add 1 on original",
     *
     *      "traceId": "Trace Id",
     *      "traceTaskId": "Trace Task Id ( Next )",
     *      "status":  "PENDING",
     *
     *      "finishedBy": null,
     *      "finishedAt": null,
     *      "createdBy": "Original created by",
     *      "createdAt": Now,
     *      "updatedBy": Current User
     *      "updatedAt": Now
     * }
     * Extra combine
     * WMove todo part such as
     * {
     *      "status": "DRAFT",
     *      "toUser": null
     * }
     */
    public static WRecord nextJ(final WRecord record, final WProcess wProcess) {
        // Todo New
        final JsonObject newJson = record.data();
        {
            // toUser -> acceptedBy
            final String toUser = newJson.getString(KName.Flow.Auditor.TO_USER);
            newJson.put(KName.Flow.Auditor.ACCEPTED_BY, toUser);
            newJson.remove(KName.Flow.Auditor.TO_USER);
        }
        final WTodo todo = record.todo();
        final WTicket ticket = record.ticket();
        WTodo entity = Ux.fromJson(newJson, WTodo.class);
        {
            // key remove
            entity.setKey(UUID.randomUUID().toString());
            // major ticket number
            entity.setTraceOrder(todo.getTraceOrder() + 1);
            entity.setSerial(ticket.getSerial() + "-" + Ut.fromAdjust(entity.getTraceOrder(), 2));
            entity.setCode(entity.getSerial());
            // Clear comments
            entity.setCommentApproval(null);
            entity.setCommentReject(null);
        }
        {
            final Task nextTask = wProcess.task();
            // entity.setTraceId(nextTask.getProcessInstanceId());
            // entity.setTraceTaskId(nextTask.getId());
            entity.setTraceId(ticket.getKey());
            entity.setTaskId(nextTask.getId());
            entity.setTaskKey(nextTask.getTaskDefinitionKey());
            entity.setStatus(TodoStatus.PENDING.name());           // Force Pending
        }
        {
            // Auditor Processing
            entity.setFinishedAt(null);
            entity.setFinishedBy(null);

            entity.setCreatedAt(LocalDateTime.now());
            entity.setCreatedBy(todo.getCreatedBy());

            entity.setUpdatedAt(LocalDateTime.now());
            entity.setUpdatedBy(todo.getUpdatedBy());
        }
        final WMoveRule rule = wProcess.ruleFind();
        if (Objects.nonNull(rule)) {
            final JsonObject todoUpdate = parseValue(rule.getTodo(), newJson);
            entity = Ux.updateT(entity, todoUpdate);
        }
        final WRecord created = WRecord.create(true, ChangeFlag.UPDATE)
            .bind(ticket)           // WTicket
            .bind(entity)           // WTodo
            .bind(record.child());  // JsonObject for Child Data
        final WRecord prev = record.prev();
        /*
         * Fix $zo has no value here
         * Here the bind must happen on matrix:
         *
         * WTodo        OLD            NEW
         *  Prev         o              x ( Will be Closed )
         *  Generated    x              o
         *
         * <Prev,New> OLD -> <null, New> New
         * The new record has been generated by OLD ( New Status )
         * In this kind of situation, the prev must be OLD ( Prev Status )
         *
         * Here are smart process:
         *
         */
        if (Objects.isNull(prev)) {
            created.prev(record);
        } else {
            created.prev(prev);
        }
        return created;
    }

    private static JsonObject parseValue(final JsonObject tpl, final JsonObject data) {
        final JsonObject normalized = new JsonObject();
        tpl.fieldNames().forEach(field -> {
            final Object value = tpl.getValue(field);
            if (value instanceof String) {
                final String valueStr = (String) value;
                if (valueStr.contains("`")) {
                    final String formatted = Ut.fromExpression(valueStr, data);
                    normalized.put(field, formatted);
                } else {
                    normalized.put(field, value);
                }
            } else {
                normalized.put(field, value);
            }
        });
        return normalized;
    }

    private static JsonObject endJ(final JsonObject params,
                                   final WProcess wProcess,
                                   final Set<String> historySet,
                                   final TodoStatus status) {
        final JsonObject todoData = params.copy();
        final String user = todoData.getString(KName.UPDATED_BY);
        /*
         * History building
         */
        final Set<String> traceSet = new HashSet<>(historySet);
        final Task task = wProcess.task();
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
            todoData.put(KName.Flow.Auditor.FINISHED_AT, Instant.now());
            todoData.put(KName.Flow.Auditor.FINISHED_BY, user);
            todoData.put(KName.Flow.FLOW_END, Boolean.TRUE);
        }
        /*
         * Closable Data
         */
        {
            // Cancel ticket will ignore closeBy
            if (TodoStatus.CANCELED == status) {
                todoData.put(KName.Flow.Auditor.CANCEL_AT, Instant.now());
                todoData.put(KName.Flow.Auditor.CANCEL_BY, user);
            }
            // Close ticket will ignore cancelBy
            if (TodoStatus.FINISHED == status) {
                todoData.put(KName.Flow.Auditor.CLOSE_AT, Instant.now());
                todoData.put(KName.Flow.Auditor.CLOSE_BY, user);
            }
        }
        return todoData;
    }
}
