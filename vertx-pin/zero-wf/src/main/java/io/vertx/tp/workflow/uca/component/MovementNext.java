package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.uca.runner.ProcOn;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementNext extends AbstractTransfer implements Movement {
    @Override
    public Future<ProcessInstance> moveAsync(final JsonObject params) {
        // Extract workflow parameters
        final JsonObject workflow = params.getJsonObject(KName.Flow.WORKFLOW);
        // Engine Connect
        final ProcOn proc = ProcOn.get();
        /* Here are parameters that needed */
        return Ux.future();
    }
}
