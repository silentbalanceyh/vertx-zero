package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementEmpty extends AbstractTransfer implements Movement {
    @Override
    public Future<ProcessInstance> moveAsync(final JsonObject params) {
        return Ux.future();
    }
}