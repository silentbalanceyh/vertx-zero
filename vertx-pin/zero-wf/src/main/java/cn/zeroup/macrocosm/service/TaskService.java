package cn.zeroup.macrocosm.service;

import cn.vertxup.workflow.domain.tables.daos.WTicketDao;
import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;

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
    public Future<WRecord> fetchRecord(final String todoKey) {
        final WRecord record = new WRecord();
        return Ux.Jooq.on(WTodoDao.class)
            .<WTodo>fetchByIdAsync(todoKey).compose(Ut.ifNil(record::bind, (todo) -> {
                // WTodo binding
                record.bind(todo);
                return Ux.Jooq.on(WTicketDao.class)
                    .<WTicket>fetchByIdAsync(todo.getTraceId())
                    .compose(Ut.ifNil(record::bind, (ticket) -> {
                        // WTicket binding
                        record.bind(ticket);
                        return Ux.future(record);
                    }));
            }));
    }

    @Override
    public Future<JsonObject> fetchPending(final String key, final String userId) {
        return this.fetchRecord(key).compose(wData -> {
            if (wData.isEmpty()) {
                return Ux.future(wData.data());
            } else {
                return wData.futureJ(false)
                    .compose(response -> this.aclStub.authorize(wData, userId)
                        .compose(acl -> Ux.future(response.put(KName.Flow.ACL, acl)))
                    );
            }
        });
    }

    @Override
    public Future<JsonObject> fetchFinished(final String key) {
        return this.fetchRecord(key).compose(wData -> {
            if (wData.isEmpty()) {
                return Ux.future(wData.data());
            } else {
                return wData.futureJ(true)
                    .compose(response -> this.aclStub.authorize(wData, null)
                        .compose(acl -> Ux.future(response.put(KName.Flow.ACL, acl)))
                    );
            }
        });
    }
}
