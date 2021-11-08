package io.vertx.tp.workflow.uca.runner;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface ProcOn {
    // StartEvent
    Future<Boolean> startAsync(String process);

    // Task
    Future<Boolean> completeAsync(String instanceId, JsonObject params);

    default Future<Boolean> completeAsync(final String instanceId) {
        return this.completeAsync(instanceId, new JsonObject());
    }
}
