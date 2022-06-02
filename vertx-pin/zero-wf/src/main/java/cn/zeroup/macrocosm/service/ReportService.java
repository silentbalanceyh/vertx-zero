package cn.zeroup.macrocosm.service;

import cn.vertxup.ambient.domain.tables.pojos.XActivity;
import cn.vertxup.ambient.domain.tables.daos.XActivityDao;
import cn.vertxup.workflow.domain.tables.daos.WTicketDao;
import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.EngineOn;
import io.vertx.tp.workflow.atom.MetaInstance;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.component.AidLinkage;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ReportService implements ReportStub {
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
    public Future<JsonObject> fetchActivity(final JsonObject condition) {
        final JsonObject combine = Ux.whereQrA(condition, "updatedAt", Boolean.TRUE);
        return Ux.Join.on()
                // Join Activity Here
                .add(XActivityDao.class, "modelKey")
                // Alias must be called after `add/join`
                .alias(XActivityDao.class, new JsonObject()
                    .put("key","key")
                )
                .searchAsync(condition);
//        final WRecord record = WRecord.create();
//        return this.readActivity(key, record)
//                // Generate JsonObject of response
//                .compose(wData -> wData.futureJ(true));
    }

    private Future<WRecord> readActivity(final String key, final WRecord response) {
        return Ux.Jooq.on(WTicketDao.class).<WTicket>fetchByIdAsync(key)
                .compose(Ut.ifNil(response::bind, ticket -> Ux.future(response.bind(ticket))));
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
                .compose(queried -> Ux.future(response.bind(queried)));
    }
}
