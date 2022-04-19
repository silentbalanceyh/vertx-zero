package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRequest;
import io.vertx.tp.workflow.uca.runner.EventOn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementPre extends AbstractTransfer implements Movement {
    @Override
    public Future<WProcess> moveAsync(final WRequest request) {
        final EventOn event = EventOn.get();
        return request.process().compose(wProcess -> event.taskActive(wProcess.instance())
            .compose(wProcess::future)          // Task Bind
            .compose(nil -> wProcess.future())
        );
    }
}
