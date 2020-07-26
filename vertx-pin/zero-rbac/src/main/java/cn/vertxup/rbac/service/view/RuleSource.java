package cn.vertxup.rbac.service.view;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.em.SourceType;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public interface RuleSource {

    ConcurrentMap<SourceType, RuleSource> EXECUTOR = new ConcurrentHashMap<SourceType, RuleSource>() {
        {
            this.put(SourceType.DAO, Ut.singleton(RuleSourceDao.class));
        }
    };

    Future<JsonObject> procAsync(JsonObject inputData, JsonObject config);
}
