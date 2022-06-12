package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.runtime.WProcess;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.refine.Wf;
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
    public Future<WProcess> moveAsync(final WRequest request) {
        // Instance Building
        return Wf.createProcess(request).compose(wProcess -> this.beforeAsync(request, wProcess).compose(normalized -> {
            /* Here normalized/request shared the same reference */
            if (wProcess.isStart()) {
                // Started
                final ProcessInstance instance = wProcess.instance();
                final Io<Task> ioTask = Io.ioTask();
                return ioTask.child(instance.getId())
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
