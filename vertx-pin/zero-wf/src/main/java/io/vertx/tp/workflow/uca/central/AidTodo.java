package io.vertx.tp.workflow.uca.central;

import cn.vertxup.workflow.domain.tables.daos.WTicketDao;
import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.configuration.MetaInstance;
import io.vertx.tp.workflow.atom.runtime.WProcess;
import io.vertx.tp.workflow.atom.runtime.WRecord;
import io.vertx.tp.workflow.uca.camunda.Io;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AidTodo {
    private final transient MetaInstance metadata;

    AidTodo(final MetaInstance metadata) {
        this.metadata = metadata;
    }

    // ------------- Generate Operation ----------------------
    public Future<WRecord> generateAsync(final WRecord record) {
        return Ux.Jooq.on(WTicketDao.class).updateAsync(record.ticket())
            .compose(ticket -> Ux.Jooq.on(WTodoDao.class).insertAsync(record.todo())
                .compose(nil -> Ux.future(record)));
    }

    // ------------- Insert Operation ----------------------
    // Save = Insert + Update
    public Future<WRecord> insertAsync(final JsonObject params, final WProcess process) {
        final ProcessInstance instance = process.instance();
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
                    final Io<Task> ioTask = Io.ioTask();
                    return ioTask.child(instance.getId())
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

    public Future<WRecord> saveAsync(final JsonObject params, final WProcess process) {
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
                return this.insertAsync(params, process);
            } else {
                final WRecord record = WRecord.create(true, ChangeFlag.UPDATE);
                return this.updateTicket(params, ticket, record)
                    .compose(processed -> this.updateChild(params, processed))
                    .compose(processed -> this.updateTodo(params, processed));
            }
        });
    }

    // ------------- Update Operation ----------------------

    public Future<WRecord> updateAsync(final JsonObject params) {
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
        /*
         * Here recordRef contains:
         * 1) Current record data
         * 2) Prev record reference
         */
        {
            // ------------- Previous ----------------------
            // Bind Original
            final WRecord prev = recordRef.prev();
            prev.bind(ticket);
        }
        final UxJooq tJq = Ux.Jooq.on(WTicketDao.class);
        final JsonObject ticketJ = params.copy();
        // Non-Update Field: key, serial, code
        ticketJ.remove(KName.KEY);
        ticketJ.remove(KName.SERIAL);
        ticketJ.remove(KName.CODE);
        // This action must happen after prev bind
        final WTicket combine = Ux.updateT(ticket, ticketJ);
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
            {
                // ------------- Previous ----------------------
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
                // ------------- Previous ----------------------
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
