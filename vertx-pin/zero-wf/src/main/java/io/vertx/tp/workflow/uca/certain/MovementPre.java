package io.vertx.tp.workflow.uca.certain;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRequest;
import io.vertx.tp.workflow.uca.central.AbstractTransfer;
import io.vertx.tp.workflow.uca.central.Movement;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.up.atom.Refer;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementPre extends AbstractTransfer implements Movement {
    @Override
    public Future<WProcess> moveAsync(final WRequest request) {
        // Instance Building
        final Refer process = new Refer();
        return this.beforeAsync(request, process).compose(normalized
            /* Here normalized/request shared the same reference */ -> {
            final EventOn event = EventOn.get();
            final WProcess wProcess = process.get();
            if (wProcess.isStart()) {
                // Started
                return event.taskActive(wProcess.instance())
                    .compose(wProcess::future)              /* Task Bind */
                    .compose(nil -> wProcess.future());
            } else {
                /*
                 *  When you stay at `e.start`, the workflow has not been started
                 *  In this kind of situation: here should return to default process
                 *  Not Started -> null
                 */
                return Ux.future(wProcess);
            }
        });
    }
}
