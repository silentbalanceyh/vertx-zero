package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WInstance;
import io.vertx.tp.workflow.atom.WKey;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.tp.workflow.uca.runner.RunOn;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementNext extends AbstractTransfer implements Movement {
    @Override
    public Future<WInstance> moveAsync(final JsonObject params) {
        // Extract workflow parameters
        final WKey key = WKey.build(params);
        final String instanceId = key.instanceId();
        // Engine Connect
        final EventOn eventOn = EventOn.get();

        // Instance Building
        final WInstance wInstance = WInstance.create();
        return Wf.instanceById(instanceId)
            // WInstance -> Bind ProcessInstance
            .compose(wInstance::future)
            .compose(instance -> {
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
                        // WInstance -> Bind Task
                        .compose(wInstance::future)
                        .compose(task -> Ux.future(this.moveGet(task.getTaskDefinitionKey())));
                }
            })
            .compose(move -> {
                // WInstance -> Bind Moving, Camunda Instance Moving
                wInstance.bind(move.stored(params));
                final ProcessInstance instance = wInstance.instance();

                // Camunda Workflow Running
                final RunOn runOn = RunOn.get();
                if (Objects.isNull(instance)) {
                    final String definitionKey = key.definitionKey();
                    return runOn.startAsync(definitionKey, move)
                        // WInstance -> Bind ProcessInstance
                        .compose(wInstance::future)
                        .compose(nil -> wInstance.future());
                } else {
                    return runOn.moveAsync(instance, move)
                        .compose(nil -> wInstance.future());
                }
            });
    }
}
