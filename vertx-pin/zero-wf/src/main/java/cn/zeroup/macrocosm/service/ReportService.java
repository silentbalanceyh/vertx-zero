package cn.zeroup.macrocosm.service;

import cn.vertxup.ambient.domain.tables.daos.XActivityDao;
import cn.vertxup.workflow.domain.tables.daos.WTicketDao;
import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

import javax.inject.Inject;

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
    public Future<JsonArray> fetchActivity(final String key, final String modelKey) {
        return Ux.Jooq.on(XActivityDao.class)
            .fetchAndAsync(new JsonObject().put("modelKey", modelKey))
            .compose(Ux::futureA)
            ;
    }

    @Override
    public Future<JsonArray> fetchActivityByUser(final String key, final String modelKey, final String userId) {
        return Ux.Jooq.on(XActivityDao.class)
            .fetchAndAsync(
                new JsonObject()
                    .put("modelKey", modelKey)
                    .put("createdBy", userId)
            )
            .compose(Ux::futureA)
            ;
    }
}
