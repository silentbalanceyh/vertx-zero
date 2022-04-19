package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRequest;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.tp.workflow.uca.runner.RunOn;
import io.vertx.up.experiment.specification.KFlow;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementNext extends AbstractTransfer implements Movement {
    @Override
    public Future<WProcess> moveAsync(final WRequest request) {
        // Extract workflow parameters
        final KFlow key = KFlow.build(request.request());
        final String instanceId = key.instanceId();

        // Instance Building
        final WProcess wProcess = WProcess.create();
        return Wf.instanceById(instanceId)
            // WProcess -> Bind ProcessInstance
            .compose(wProcess::future)
            .compose(instance -> {
                // Engine Connect
                final EventOn eventOn = EventOn.get();
                // Whether instance existing
                if (Objects.isNull(instance)) {
                    /*
                     * instance does not exist in your system
                     * Call Start Process
                     * -- WMove
                     */
                    final String definitionId = key.definitionId();
                    return eventOn.start(definitionId)
                        .compose(event -> Ux.future(this.moveGet(event.getId())));
                } else {
                    /*
                     * instance exist in your system
                     * Call Next Process with active task
                     * -- WMove ( Current task Move )
                     */
                    final String taskId = key.taskId();
                    return eventOn.taskSmart(instance, taskId)
                        // WProcess -> Bind Task
                        .compose(wProcess::future)
                        .compose(task -> Ux.future(this.moveGet(task.getTaskDefinitionKey())));
                }
            })
            .compose(move -> {
                // WProcess -> Bind Moving, Camunda Instance Moving
                wProcess.bind(move.stored(request.request()));
                final ProcessInstance instance = wProcess.instance();

                // Camunda Workflow Running
                final RunOn runOn = RunOn.get();
                if (Objects.isNull(instance)) {
                    final String definitionKey = key.definitionKey();
                    return runOn.startAsync(definitionKey, move)
                        // WProcess -> Bind ProcessInstance
                        .compose(wProcess::future)
                        .compose(nil -> wProcess.future());
                } else {
                    return runOn.moveAsync(instance, move)
                        .compose(nil -> wProcess.future());
                }
            });
    }
}
