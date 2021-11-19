package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WInstance;
import io.vertx.tp.workflow.atom.WKey;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.runner.EventOn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementPre extends AbstractTransfer implements Movement {
    @Override
    public Future<WInstance> moveAsync(final JsonObject params) {
        final WKey key = WKey.build(params);
        final WInstance wInstance = WInstance.create();
        final EventOn event = EventOn.get();
        return Wf.instanceById(key.instanceId())
            .compose(wInstance::future)
            .compose(nil -> event.taskActive(wInstance.instance()))
            .compose(wInstance::future)
            .compose(nil -> wInstance.future());
    }
}
