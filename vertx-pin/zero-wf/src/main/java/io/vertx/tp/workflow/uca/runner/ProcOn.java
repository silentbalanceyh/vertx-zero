package io.vertx.tp.workflow.uca.runner;

import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface ProcOn {

    static ProcOn get() {
        return Fn.poolThread(WfPool.POOL_PROC, ProcEngine::new);
    }

    // StartEvent
    Future<Boolean> startAsync(String process);

    // Task
    Future<Boolean> completeAsync(String instanceId, JsonObject params);

    default Future<Boolean> completeAsync(final String instanceId) {
        return this.completeAsync(instanceId, new JsonObject());
    }
}
