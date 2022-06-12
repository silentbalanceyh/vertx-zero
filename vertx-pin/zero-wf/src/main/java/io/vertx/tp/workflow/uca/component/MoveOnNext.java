package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.error._409InValidInstanceException;
import io.vertx.tp.workflow.atom.runtime.WMove;
import io.vertx.tp.workflow.atom.runtime.WProcess;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.uca.camunda.RunOn;
import io.vertx.tp.workflow.uca.central.AbstractMoveOn;
import io.vertx.tp.workflow.uca.conformity.Gear;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.up.experiment.specification.KFlow;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MoveOnNext extends AbstractMoveOn {
    @Override
    public Future<WProcess> moveAsync(final WRequest request, final WProcess process) {
        final ProcessInstance instance = process.instance();
        final KFlow key = request.workflow();
        final String instanceId = key.instanceId();
        if (Objects.isNull(instance)) {
            return Ux.thenError(_409InValidInstanceException.class, this.getClass(), instanceId);
        }
        /*
         * instance exist in your system
         * 1. Call Next Process with active task
         * 2. Get the WMove of current active task
         * 3. Camunda Instance Moving to next
         */
        final String taskId = key.taskId();
        final Gear scatter = process.scatter();

        final EventOn eventOn = EventOn.get();
        return eventOn.taskOldSmart(instance, taskId)
            /* WProcess -> Bind Task */
            .compose(task -> Ux.future(process.bind(task)))
            .compose(nil -> {
                final Task task = process.task();
                // WMove Get
                final WMove move = this.rule(task.getTaskDefinitionKey()).stored(request.request());
                process.bind(move);
                // Camunda Workflow Running
                final RunOn runOn = RunOn.get();
                return runOn.moveAsync(instance, move);
            })
            .compose(nil -> process.future());
    }
}
