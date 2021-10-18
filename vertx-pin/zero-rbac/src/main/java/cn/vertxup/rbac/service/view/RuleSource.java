package cn.vertxup.rbac.service.view;

import cn.vertxup.rbac.service.view.source.RadixGroup;
import cn.vertxup.rbac.service.view.source.RadixUi;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.em.SourceGroup;
import io.vertx.tp.rbac.cv.em.SourceType;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface RuleSource {

    ConcurrentMap<SourceType, RuleSource> UIS = new ConcurrentHashMap<SourceType, RuleSource>() {
        {
            this.put(SourceType.DAO, Ut.singleton(RadixUi.class));
        }
    };

    ConcurrentMap<SourceGroup, RuleSource> GROUPS = new ConcurrentHashMap<SourceGroup, RuleSource>() {
        {
            this.put(SourceGroup.DAO, Ut.singleton(RadixGroup.class));
        }
    };

    Future<JsonObject> procAsync(JsonObject inputData, JsonObject config);
}
