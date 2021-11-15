package cn.zeroup.macrocosm.service;

import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TaskService implements TaskStub {
    @Override
    public Future<JsonObject> fetchQueue(final JsonObject condition) {
        final JsonObject normalized = Ux.whereQrA(condition, KName.INSTANCE, Boolean.TRUE);
        // Condition / Connect to Task of Camunda Platform
        return Ux.Jooq.on(WTodoDao.class).searchAsync(normalized);
    }
}
