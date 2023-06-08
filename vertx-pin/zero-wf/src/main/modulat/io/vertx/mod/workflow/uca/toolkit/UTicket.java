package io.vertx.mod.workflow.uca.toolkit;

import cn.vertxup.workflow.domain.tables.daos.WTicketDao;
import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.horizon.atom.common.Refer;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.configuration.MetaInstance;
import io.vertx.mod.workflow.atom.runtime.WRecord;
import io.vertx.mod.workflow.atom.runtime.WTransition;
import io.vertx.mod.workflow.uca.ticket.Sync;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class UTicket {
    private final transient MetaInstance metadata;

    public UTicket(final MetaInstance metadata) {
        this.metadata = metadata;
    }

    // ------------- Generate Operation ----------------------
    public Future<WRecord> generateAsync(final JsonObject requestJ, final WTransition wTransition, final WRecord record) {
        final WTicket ticket = record.ticket();
        Objects.requireNonNull(ticket);
        final WRecord generatedNew = UTL.recordU(wTransition);

        // AOP/Before
        return UTL.beforeGenerate(record, generatedNew).compose(generated -> Ux.Jooq.on(WTicketDao.class).<WTicket>fetchByIdAsync(ticket.getKey())

            // Sync Ticket
            .compose(processed -> Sync.ticket(this.metadata).treatAsync(requestJ, generated, processed))

            // Sync Extension
            .compose(recordRef -> Sync.extension(this.metadata).treatAsync(requestJ, recordRef))

            // AOP/Before
            .compose(nil -> UTL.beforeUpdate(generated, record.task(), requestJ))

            // Sync
            .compose(nil -> {
                final WTodo todo = record.task();
                /*
                 * Generation based data should be
                 * Original WTodo Json + Input RequestJ here to combine
                 * json content for generation new WTodo
                 */
                final JsonObject todoJ = Ux.toJson(todo);
                todoJ.mergeIn(requestJ, true);
                return wTransition.end(todoJ, generated.ticket(), todo);
            })
            .compose(Ux.Jooq.on(WTodoDao.class)::insertAsync)

            // AOP/After
            .compose(tasks -> UTL.afterUpdate(tasks, generated))
        );
    }

    // ------------- Insert Operation ----------------------
    // Save = Insert + Update
    public Future<WRecord> insertAsync(final JsonObject params, final WTransition wTransition) {
        return this.insertTicket(params, wTransition).compose(record -> {
            final WTicket inserted = record.ticket();
            final JsonObject gearInput = params.copy();
            return wTransition.end(gearInput, inserted)
                .compose(Ux.Jooq.on(WTodoDao.class)::insertAsync)

                // AOP/After
                .compose(tasks -> UTL.afterUpdate(tasks, record));
        });
    }


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
    public Future<WRecord> saveAsync(final JsonObject params, final WTransition wTransition) {
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
                return this.insertAsync(params, wTransition);
            } else {
                final WRecord record = UTL.recordU(wTransition);

                // Sync Ticket
                return Sync.ticket(this.metadata).treatAsync(params, record, ticket)

                    // Sync Extension
                    .compose(recordRef -> Sync.extension(this.metadata).treatAsync(params, recordRef))

                    // Sync Task
                    .compose(processed -> Sync.task(this.metadata).treatAsync(params, processed));
            }
        });
    }

    public Future<WRecord> updateAsync(final JsonObject params, final WTransition wTransition) {
        /*
         * Ticket Data Updating
         * 1. Extract key from `traceId` field
         * 2. Remove `key` because here the `key` field is W_TODO
         */
        final String tKey = params.getString(KName.Flow.TRACE_ID);
        final WRecord record = UTL.recordU(wTransition);
        /*
         * Steps:
         * 1. WTicket
         * 2. Extension
         * 3. WTodo
         */
        return Ux.Jooq.on(WTicketDao.class).<WTicket>fetchByIdAsync(tKey)

            // Sync Ticket
            .compose(processed -> Sync.ticket(this.metadata).treatAsync(params, record, processed))

            // Sync Extension
            .compose(recordRef -> Sync.extension(this.metadata).treatAsync(params, recordRef))

            // Sync Task
            .compose(processed -> Sync.task(this.metadata).treatAsync(params, processed));
    }

    // ------------- Private Update Operation ----------------------
    private Future<WRecord> insertTicket(final JsonObject params, final WTransition wTransition) {
        // Todo Build
        final Refer refer = new Refer();
        return this.metadata.todoInitialize(params).compose(refer::future)
            .compose(normalized -> {
                // Ticket Workflow ( normalized = params )
                final JsonObject ticketJ = normalized.copy();
                ticketJ.remove(KName.KEY);
                final WTicket ticket = Ux.fromJson(ticketJ, WTicket.class);
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
                ticket.setKey(normalized.getString(KName.Flow.TRACE_KEY));      // Connect ticket key
                ticket.setFlowEnd(Boolean.FALSE);
                final ProcessInstance instance = wTransition.instance();
                ticket.setFlowInstanceId(instance.getId());
                return Ux.Jooq.on(WTicketDao.class).insertAsync(ticket);
            })

            // AOP/After
            .compose(ticket -> UTL.afterInsert(ticket, wTransition))

            // Sync Extension
            .compose(recordRef -> Sync.extension(this.metadata).treatAsync(refer.get(), recordRef));
    }
}
