package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.daos.WTicketDao;
import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.tp.workflow.atom.WInstance;
import io.vertx.tp.workflow.atom.WMoveRule;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.runtime.ProcessInstance;
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
class KitTodo {
    /*
     * Close current
     * {
     *      "status": "FINISHED",
     *      "finishedAt": "",
     *      "finishedBy": "",
     *      "traceEnd": "Depend on instance",
     *      "active": true
     * }
     */
    static JsonObject closeJ(final JsonObject params, final WInstance wInstance) {
        final JsonObject updatedData = params.copy();
        updatedData.put(KName.STATUS, TodoStatus.FINISHED);
        final String user = params.getString(KName.UPDATED_BY);
        updatedData.put(KName.Flow.Auditor.FINISHED_AT, Instant.now());
        updatedData.put(KName.Flow.Auditor.FINISHED_BY, user);
        // updatedAt / updatedBy contain values
        updatedData.put(KName.ACTIVE, Boolean.TRUE);

        if (wInstance.isEnd()) {
            updatedData.put(KName.Flow.FLOW_END, Boolean.TRUE);
        }
        // Todo based on previous
        final WMoveRule rule = wInstance.rule();
        if (Objects.nonNull(rule) && Ut.notNil(rule.getTodo())) {
            updatedData.mergeIn(rule.getTodo());
        }
        return updatedData;
    }

    /*
     * Modification
     * {
     *      "traceEnd": true
     *      "traceExtra":{
     *          "history": "ADD"
     *      },
     *      "status": "CANCELED",
     *      "finishedAt": "",
     *      "finishedBy": ""
     * }
     */
    static JsonObject cancelJ(final JsonObject params, final WInstance wInstance, final Set<String> historySet) {
        final JsonObject todoData = params.copy();
        /*
         * History building
         */
        final Set<String> traceSet = new HashSet<>(historySet);
        final Task task = wInstance.task();
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

            final String user = todoData.getString(KName.UPDATED_BY);
            todoData.put(KName.STATUS, TodoStatus.CANCELED.name());
            todoData.put(KName.Flow.Auditor.FINISHED_AT, Instant.now());
            todoData.put(KName.Flow.Auditor.FINISHED_BY, user);
            todoData.put(KName.Flow.FLOW_END, Boolean.TRUE);
        }
        return todoData;
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
    static WRecord nextJ(final WRecord record, final WInstance wInstance) {
        // Todo New
        final JsonObject newJson = record.data();
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
            final Task nextTask = wInstance.task();
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
        final WMoveRule rule = wInstance.rule();
        if (Objects.nonNull(rule)) {
            final JsonObject todoUpdate = rule.getTodo();
            entity = Ux.updateT(entity, todoUpdate);
        }
        return new WRecord().bind(ticket).bind(entity);
    }

    Future<WRecord> saveAsync(final JsonObject params, final ConfigTodo config,
                              final ProcessInstance instance) {
        /*
         * Ticket Data Updating
         * 1. Fetch record by `traceId` field
         * 2. If null, create new ticket with todo ( Closed )
         */
        final JsonObject ticketJson = params.copy();
        final String tKey = ticketJson.getString(KName.Flow.TRACE_ID);
        final UxJooq tJq = Ux.Jooq.on(WTicketDao.class);
        return tJq.<WTicket>fetchByIdAsync(tKey).compose(ticket -> {
            if (Objects.isNull(ticket)) {
                return this.insertAsync(params, config, instance);
            } else {
                return this.updateAsync(params);
            }
        });
    }

    Future<WRecord> updateAsync(final JsonObject params) {
        /*
         * Ticket Data Updating
         * 1. Extract key from `traceId` field
         * 2. Remove `key` because here the `key` field is W_TODO
         */
        final JsonObject ticketJson = params.copy();
        ticketJson.remove(KName.KEY);
        final String tKey = ticketJson.getString(KName.Flow.TRACE_ID);
        return this.updateTicket(tKey, ticketJson).compose(ticket -> {
            /*
             * Todo Data Updating
             * Updating the todo record for future usage
             */
            final UxJooq jooq = Ux.Jooq.on(WTodoDao.class);
            final String key = params.getString(KName.KEY);
            return jooq.<WTodo>fetchByIdAsync(key).compose(query -> {
                final WTodo updated = Ux.updateT(query, params);
                return jooq.updateAsync(updated);
            }).compose(todo -> WRecord.future(ticket, todo));
        });
    }

    Future<WRecord> generateAsync(final WRecord record) {
        return Ux.Jooq.on(WTicketDao.class).updateAsync(record.ticket())
            .compose(ticket -> Ux.Jooq.on(WTodoDao.class).insertAsync(record.todo())
                .compose(nil -> Ux.future(record)));
    }

    private Future<WTicket> updateTicket(final String key, final JsonObject params) {
        final UxJooq tJq = Ux.Jooq.on(WTicketDao.class);
        return tJq.<WTicket>fetchByIdAsync(key).compose(ticket -> {
            final WTicket updated = Ux.updateT(ticket, params);
            return tJq.updateAsync(updated);
        });
    }

    // ------------- Todo Insert ----------------------
    Future<WRecord> insertAsync(final JsonObject params, final ConfigTodo config,
                                final ProcessInstance instance) {
        // Todo Build
        return config.generate(params).compose(normalized -> {
            // Ticket Workflow
            final String todoKey = normalized.getString(KName.KEY);
            normalized.remove(KName.KEY);
            final WTicket ticket = Ux.fromJson(normalized, WTicket.class);
            /*
             * null value when ticket processed
             *
             *  - code: came from serial
             * 「Camunda」
             *  - flowDefinitionKey: came from json
             *  - flowDefinitionId: came from json
             *  - flowInstanceId: came from process
             *  - flowEnd: false when insert todo
             *
             * 「Flow」
             *  - cancelBy
             *  - cancelAt
             *  - closeBy
             *  - closeAt
             *  - closeSolution
             *  - closeCode
             *
             * 「Future」
             *  - metadata
             *  - modelCategory
             *  - category
             *  - categorySub
             */
            ticket.setFlowEnd(Boolean.FALSE);
            ticket.setFlowInstanceId(instance.getId());
            return Ux.Jooq.on(WTicketDao.class).insertAsync(ticket).compose(inserted -> {
                // Todo Workflow
                final WTodo todo = Ux.fromJson(normalized, WTodo.class);
                /*
                 * Key Point: The todo key will be used in `todoUrl` field here,
                 * it means that we must set the `key` fixed to avoid todoUrl capture
                 * the key of ticket.
                 */
                todo.setKey(todoKey);
                /*
                 * null value when processed
                 * 「Related」
                 *  - traceId
                 *  - traceOrder
                 *  - parentId
                 *
                 * 「Camunda」
                 *  - taskId
                 *  - taskKey
                 *
                 * 「Flow」
                 *  - assignedBy
                 *  - assignedAt
                 *  - acceptedBy
                 *  - acceptedAt
                 *  - finishedBy
                 *  - finishedAt
                 *  - comment
                 *  - commentApproval
                 *  - commentReject
                 *
                 * 「Future」
                 *  - metadata
                 *  - modelCategory
                 *  - activityId
                 */
                todo.setTraceId(inserted.getKey());
                todo.setTraceOrder(1);
                todo.setCode(inserted.getCode() + "-" + Ut.fromAdjust(todo.getTraceOrder(), 2));
                todo.setSerial(inserted.getSerial() + "-" + Ut.fromAdjust(todo.getTraceOrder(), 2));


                /*
                 *  Connect WTodo and ProcessInstance
                 *  1. taskId = Task, getId
                 *  2. taskKey = Task, getTaskDefinitionKey
                 */
                final EventOn event = EventOn.get();
                return event.taskActive(instance)
                    .compose(task -> {
                        // Camunda Engine
                        todo.setTaskId(task.getId());
                        todo.setTaskKey(task.getTaskDefinitionKey());        // Task Key/Id
                        return Ux.future(todo);
                    })
                    .compose(Ux.Jooq.on(WTodoDao.class)::insertAsync)
                    .compose(insertedTodo -> WRecord.future(inserted, insertedTodo));
            });
        });
    }
}
