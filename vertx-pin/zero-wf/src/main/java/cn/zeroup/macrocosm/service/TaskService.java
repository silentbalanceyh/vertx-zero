package cn.zeroup.macrocosm.service;

import cn.vertxup.workflow.domain.tables.daos.WTicketDao;
import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.EngineOn;
import io.vertx.tp.workflow.atom.configuration.MetaInstance;
import io.vertx.tp.workflow.atom.runtime.WRecord;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.toolkit.ULinkage;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TaskService implements TaskStub {
    @Inject
    private transient AclStub aclStub;

    @Override
    public Future<JsonObject> fetchQueue(final JsonObject condition) {
        final JsonObject combine = Ux.whereQrA(condition, KName.Flow.FLOW_END, Boolean.FALSE);
        return Ux.Join.on()

            // Join WTodo Here
            .add(WTodoDao.class, KName.Flow.TRACE_ID)
            .join(WTicketDao.class)

            // Alias must be called after `add/join`
            .alias(WTicketDao.class, new JsonObject()
                .put(KName.KEY, KName.Flow.TRACE_KEY)
                .put(KName.SERIAL, KName.Flow.TRACE_SERIAL)
                .put(KName.CODE, KName.Flow.TRACE_CODE)
            )
            .searchAsync(combine);
    }

    @Override
    public Future<JsonObject> fetchHistory(final JsonObject condition) {
        final JsonObject combine = Ux.whereQrA(condition, KName.Flow.FLOW_END, Boolean.TRUE);
        return Ux.Jooq.on(WTicketDao.class).searchAsync(combine);
    }

    // ====================== Single Record
    @Override
    public Future<JsonObject> readPending(final String key, final String userId) {
        final WRecord record = WRecord.create();


        // Read Todo Record
        // Extract traceId from WTodo
        return this.readTodo(key, record).compose(processed -> {
            final WTodo todo = processed.todo();
            if (Objects.isNull(todo)) {
                Wf.Log.infoWeb(this.getClass(), "Ticket Status Conflict, key = {0}", key);
                return Ux.futureJ();
            } else {
                return Ux.future(todo.getTraceId())

                    // Read Ticket Record
                    .compose(ticketId -> this.readTicket(ticketId, record))


                    // Linkage
                    .compose(ULinkage::readLinkage)


                    // Child
                    .compose(this::readChild)


                    // Generate JsonObject of response
                    .compose(wData -> wData.futureJ(false))


                    // Acl Mount
                    .compose(response -> this.aclStub.authorize(record, userId)
                        .compose(acl -> Ux.future(response.put(KName.__.ACL, acl)))
                    );
            }
        });
    }

    private Future<WRecord> readTodo(final String key, final WRecord response) {
        return Ux.Jooq.on(WTodoDao.class).<WTodo>fetchByIdAsync(key)
            .compose(Ut.ifNil(response::bind, todo -> Ux.future(response.task(todo))));
    }

    private Future<WRecord> readTicket(final String key, final WRecord response) {
        return Ux.Jooq.on(WTicketDao.class).<WTicket>fetchByIdAsync(key)
            .compose(Ut.ifNil(response::bind, ticket -> Ux.future(response.ticket(ticket))));
    }

    private Future<WRecord> readChild(final WRecord response) {
        final WTicket ticket = response.ticket();
        Objects.requireNonNull(ticket);

        // Connect to Workflow Engine
        final EngineOn engine = EngineOn.connect(ticket.getFlowDefinitionKey());
        final MetaInstance meta = engine.metadata();

        // Read Child
        final UxJooq jq = meta.childDao();
        if (Objects.isNull(jq)) {
            return Ux.future(response);
        }
        return jq.fetchJByIdAsync(ticket.getKey())
            // ChildOut
            .compose(queried -> Ux.future(meta.childOut(queried)))
            .compose(queried -> Ux.future(response.ticket(queried)));
    }


    @Override
    public Future<JsonObject> readFinished(final String key) {
        final WRecord record = WRecord.create();


        // Read Ticket Record
        return this.readTicket(key, record)


            // Linkage
            .compose(ULinkage::readLinkage)


            // Child
            .compose(this::readChild)


            // Generate JsonObject of response
            .compose(wData -> wData.futureJ(true));
    }
}
