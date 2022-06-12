package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.runtime.WProcess;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.central.AbstractTransfer;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementStay extends AbstractTransfer implements Movement {
    @Override
    public Future<WProcess> moveAsync(final WRequest request) {
        // Instance Building
        return Wf.process(request).compose(wProcess -> this.beforeAsync(request, wProcess)
            .compose(normalized -> {
                /* Here normalized/request shared the same reference */
                final EventOn event = EventOn.get();
                if (wProcess.isStart()) {
                    // Started
                    return event.taskOldActive(wProcess.instance())
                        /* Task Bind */
                        .compose(task -> Ux.future(wProcess.bind(task)))
                        .compose(nil -> wProcess.future());
                } else {
                    /*
                     *  When you stay at `e.start`, the workflow has not been started
                     *  In this kind of situation: here should return to default process
                     *  Not Started -> null
                     */
                    return Ux.future(wProcess);
                }
            }));
    }
}
