package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.daos.WTicketDao;
import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.MetaInstance;
import io.vertx.tp.workflow.atom.WMoveRule;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
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
class AidTodo {
    private final transient MetaInstance metadata;

    AidTodo(final MetaInstance metadata) {
        this.metadata = metadata;
    }

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
    static JsonObject closeJ(final JsonObject params, final WProcess wProcess) {
        final JsonObject updatedData = params.copy();
        updatedData.put(KName.STATUS, TodoStatus.FINISHED.name());
        final String user = params.getString(KName.UPDATED_BY);
        updatedData.put(KName.Flow.Auditor.FINISHED_AT, Instant.now());
        updatedData.put(KName.Flow.Auditor.FINISHED_BY, user);
        // updatedAt / updatedBy contain values
        updatedData.put(KName.ACTIVE, Boolean.TRUE);

        /*
         * Closable Data
         */
        updatedData.put(KName.Flow.Auditor.CLOSE_AT, Instant.now());
        updatedData.put(KName.Flow.Auditor.CLOSE_BY, user);

        if (wProcess.isEnd()) {
            updatedData.put(KName.Flow.FLOW_END, Boolean.TRUE);
        }
        // Todo based on previous
        final WMoveRule rule = wProcess.ruleFind();
        if (Objects.nonNull(rule) && Ut.notNil(rule.getTodo())) {
            final JsonObject parsed = parseValue(rule.getTodo(), params);
            updatedData.mergeIn(parsed);
        }
        return updatedData;
    }

    /*
     * Modification
     * {
     *      "flowEnd": true
     *      "status": "CANCELED",
     *      "finishedAt": "",
     *      "finishedBy": ""
     * }
     */
    static JsonObject cancelJ(final JsonObject params, final WProcess wProcess, final Set<String> historySet) {
        return endJ(params, wProcess, historySet, TodoStatus.CANCELED);
    }

    static JsonObject closeJ(final JsonObject params, final WProcess wProcess, final Set<String> historySet) {
        return endJ(params, wProcess, historySet, TodoStatus.FINISHED);
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
    static WRecord nextJ(final WRecord record, final WProcess wProcess) {
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
        return WRecord.create(true, ChangeFlag.UPDATE)
            .prev(record)   // Fix $zo has no value here
            .bind(ticket).bind(entity);
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

    // ------------- Generate Operation ----------------------
    Future<WRecord> generateAsync(final WRecord record) {
        return Ux.Jooq.on(WTicketDao.class).updateAsync(record.ticket())
            .compose(ticket -> Ux.Jooq.on(WTodoDao.class).insertAsync(record.todo())
                .compose(nil -> Ux.future(record)));
    }

    // ------------- Insert Operation ----------------------
    // Save = Insert + Update
    Future<WRecord> insertAsync(final JsonObject params, final ProcessInstance instance) {
        // Todo Build
        return this.metadata.todoInitialize(params).compose(normalized -> {
            // Ticket Workflow
            final String todoKey = normalized.getString(KName.KEY);
            normalized.remove(KName.KEY);
            final WTicket ticket = Ux.fromJson(normalized, WTicket.class);
            ticket.setKey(normalized.getString(KName.Flow.TRACE_KEY));      // Connect ticket key
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
            final WRecord record = WRecord.create(true, ChangeFlag.ADD);
            return Ux.Jooq.on(WTicketDao.class).insertAsync(ticket)
                .compose(inserted -> this.updateChild(normalized, record.bind(inserted)))
                .compose(processed -> {
                    final WTicket inserted = processed.ticket();
                    /*
                     * Key Point for attachment linkage here, the linkage must contain
                     * serial part in params instead of distinguish between ADD / EDIT
                     */
                    if (!params.containsKey(KName.SERIAL)) {
                        params.put(KName.SERIAL, inserted.getSerial());
                    }
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
                        .compose(insertedTodo -> {
                            /*
                             * No `status` field here.
                             */
                            record.bind(insertedTodo);
                            return Ux.future(record);
                        });
                });
        });
    }

    Future<WRecord> saveAsync(final JsonObject params, final ProcessInstance instance) {
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
                /*
                 * Code Logical 1:
                 * Here the system will create new ticket, it means that related `MODEL_KEY` is null, the structure is:
                 * {
                 *      "key": null,
                 *      "record": {
                 *          "key": null or has value
                 *      }
                 * }
                 * We should prepare the whole key related here to build relationship between
                 * -- WTicket + Extension Ticket
                 * -- WTicket + Extension Entity
                 *
                 */
                return this.insertAsync(params, instance);
            } else {
                final WRecord record = WRecord.create(true, ChangeFlag.UPDATE);
                return this.updateTicket(params, ticket, record)
                    .compose(processed -> this.updateChild(params, processed))
                    .compose(processed -> this.updateTodo(params, processed));
            }
        });
    }

    // ------------- Update Operation ----------------------

    Future<WRecord> updateAsync(final JsonObject params) {
        /*
         * Ticket Data Updating
         * 1. Extract key from `traceId` field
         * 2. Remove `key` because here the `key` field is W_TODO
         */
        final String tKey = params.getString(KName.Flow.TRACE_ID);
        final WRecord record = WRecord.create(true, ChangeFlag.UPDATE);
        return Ux.Jooq.on(WTicketDao.class).<WTicket>fetchByIdAsync(tKey)
            .compose(ticket -> this.updateTicket(params, ticket, record))
            .compose(processed -> this.updateChild(params, processed))
            .compose(processed -> this.updateTodo(params, processed));
    }

    // ------------- Private Update Operation ----------------------

    private Future<WRecord> updateTicket(final JsonObject params, final WTicket ticket, final WRecord recordRef) {
        Objects.requireNonNull(recordRef.prev());
        final UxJooq tJq = Ux.Jooq.on(WTicketDao.class);
        final JsonObject ticketJ = params.copy();
        // Non-Update Field: key, serial, code
        ticketJ.remove(KName.KEY);
        ticketJ.remove(KName.SERIAL);
        ticketJ.remove(KName.CODE);
        final WTicket combine = Ux.updateT(ticket, ticketJ);
        /*
         * Here recordRef contains:
         * 1) Current record data
         * 2) Prev record reference
         */
        {
            // Bind Original
            final WRecord prev = recordRef.prev();
            prev.bind(ticket);
        }
        return tJq.updateAsync(combine).compose(updated -> {
            // Bind Updated
            /*
             * Key Point for attachment linkage here, the linkage must contain
             * serial part in params instead of distinguish between ADD / EDIT
             */
            if (!params.containsKey(KName.SERIAL)) {
                params.put(KName.SERIAL, ticket.getSerial());
            }
            recordRef.bind(updated);
            return Ux.future(recordRef);
        });
    }


    private Future<WRecord> updateTodo(final JsonObject params, final WRecord recordRef) {
        final UxJooq tJq = Ux.Jooq.on(WTodoDao.class);
        final String key = params.getString(KName.KEY);
        return tJq.<WTodo>fetchByIdAsync(key).compose(query -> {
            /*
             * Critical Step for status binding
             * and the recordRef must bind original status
             */
            {
                // Bind Original
                final WRecord prev = recordRef.prev();
                prev.bind(query);
            }

            final JsonObject todoJ = params.copy();
            // Non-Update Field: key, serial, code
            todoJ.remove(KName.KEY);
            todoJ.remove(KName.SERIAL);
            todoJ.remove(KName.CODE);
            final WTodo updated = Ux.updateT(query, todoJ);
            return tJq.updateAsync(updated).compose(todo -> {
                // Bind Updated
                recordRef.bind(todo);
                return Ux.future(recordRef);
            });
        });
    }

    private Future<WRecord> updateChild(final JsonObject params, final WRecord recordRef) {
        final UxJooq tJq = this.metadata.childDao();
        if (Objects.isNull(tJq)) {
            return Ux.future(recordRef);
        }
        // JsonObject data for child
        final JsonObject data = this.metadata.childData(params);
        // Shared `key` between ticket / child ticket
        final WTicket ticket = recordRef.ticket();
        data.put(KName.KEY, ticket.getKey());
        return tJq.fetchJOneAsync(KName.KEY, ticket.getKey()).compose(queryJ -> {
            Objects.requireNonNull(queryJ);
            // Bind Original
            {
                final WRecord prev = recordRef.prev();
                prev.bind(queryJ);
            }

            // Updated Json for Child
            final JsonObject combineJ = queryJ.copy().mergeIn(data, true);
            // Fix issue of `Sub Table Empty"
            if (Ut.isNil(queryJ)) {
                // Does not Exist
                return tJq.insertJAsync(combineJ);
            } else {
                // Existing
                /*
                 * class cn.vertxup.workflow.domain.tables.pojos.TAssetIn cannot be
                 * cast to class io.vertx.core.json.JsonObject
                 *
                 * Here must use updateJAsync
                 */
                return tJq.updateJAsync(ticket.getKey(), combineJ);
            }
        }).compose(updated -> {
            // Bind Updated
            recordRef.bind(updated);
            return Ux.future(recordRef);
        });
    }
}
