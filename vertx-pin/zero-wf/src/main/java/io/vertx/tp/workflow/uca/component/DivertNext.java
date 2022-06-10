package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.error._409InValidInstanceException;
import io.vertx.tp.workflow.atom.WMove;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRequest;
import io.vertx.tp.workflow.uca.canal.AbstractDivert;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.tp.workflow.uca.runner.RunOn;
import io.vertx.up.experiment.specification.KFlow;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DivertNext extends AbstractDivert {
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
        final EventOn eventOn = EventOn.get();
        return eventOn.taskSmart(instance, taskId)
            .compose(process::future /* WProcess -> Bind Task */)
            .compose(task -> {
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
