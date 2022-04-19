package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.up.experiment.specification.KFlow;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementPre extends AbstractTransfer implements Movement {
    @Override
    public Future<WProcess> moveAsync(final JsonObject params) {
        final KFlow key = KFlow.build(params);
        final WProcess wProcess = WProcess.create();
        final EventOn event = EventOn.get();
        return Wf.instanceById(key.instanceId())
            .compose(wProcess::future)
            .compose(nil -> event.taskActive(wProcess.instance()))
            .compose(wProcess::future)
            .compose(nil -> wProcess.future());
    }
}
