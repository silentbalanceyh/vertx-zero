package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.atom.runtime.WTransition;
import io.vertx.tp.workflow.uca.camunda.Io;
import io.vertx.tp.workflow.uca.central.AbstractTransfer;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementStay extends AbstractTransfer implements Movement {
    @Override
    public Future<WTransition> moveAsync(final WRequest request) {
        final WTransition wTransition = WTransition.create(request, this.rules());
        // Instance Building
        return wTransition.start()
            .compose(started -> this.trackerKit.beforeAsync(request, started))
            .compose(normalized -> {
                /* Here normalized/request shared the same reference */
                if (wTransition.isStarted()) {
                    // Started
                    final ProcessInstance instance = wTransition.instance();
                    final Io<Task> ioTask = Io.ioTask();
                    return ioTask.child(instance.getId())
                        /* Task Bind */
                        .compose(task -> Ux.future(wTransition.from(task)))
                        .compose(nil -> Ux.future(wTransition));
                } else {
                    /*
                     *  When you stay at `e.start`, the workflow has not been started
                     *  In this kind of situation: here should return to default process
                     *  Not Started -> null
                     */
                    return Ux.future(wTransition);
                }
            });
    }
}
