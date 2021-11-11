package io.vertx.tp.workflow.uca.runner;

import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface StoreOn {
    static StoreOn get() {
        return Fn.poolThread(WfPool.POOL_STORE, StoreEngine::new);
    }

    Future<JsonObject> fetchFlow(String code);
}
