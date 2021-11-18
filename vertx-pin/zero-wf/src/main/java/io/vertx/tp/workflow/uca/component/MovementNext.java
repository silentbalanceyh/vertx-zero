package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.tp.workflow.uca.runner.RunOn;
import io.vertx.up.atom.Refer;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementNext extends AbstractTransfer implements Movement {
    @Override
    public Future<ProcessInstance> moveAsync(final JsonObject params) {
        // Extract workflow parameters
        final JsonObject workflow = params.getJsonObject(KName.Flow.WORKFLOW);
        final String instanceId = workflow.getString(KName.Flow.INSTANCE_ID);
        // Engine Connect
        final EventOn eventOn = EventOn.get();
        final Refer instanceRef = new Refer();
        return Wf.instanceById(instanceId)
            .compose(instanceRef::future)
            .compose(instance -> {
                // Whether instance existing
                if (Objects.isNull(instance)) {
                    /*
                     * instance does not exist in your system
                     * Call Start Process
                     * -- WMove
                     */
                    final String definitionId = workflow.getString(KName.Flow.DEFINITION_ID);
                    return eventOn.start(definitionId)
                        .compose(event -> Ux.future(this.moveGet(event.getId())));
                } else {
                    /*
                     * instance exist in your system
                     * Call Next Process with active task
                     * -- WMove ( Current task Move )
                     */
                    final String taskId = workflow.getString(KName.Flow.TASK_ID);
                    return eventOn.taskSmart(instance, taskId)
                        .compose(task -> Ux.future(this.moveGet(task.getTaskDefinitionKey())));
                }
            })
            .compose(move -> {
                // Camunda Instance Moving
                final JsonObject wParams = this.moveData(params, move);
                final ProcessInstance instance = instanceRef.get();

                // Camunda Workflow Running
                final RunOn runOn = RunOn.get();
                if (Objects.isNull(instance)) {
                    final String definitionKey = workflow.getString(KName.Flow.DEFINITION_KEY);
                    return runOn.startAsync(definitionKey, wParams);
                } else {
                    return runOn.moveAsync(instance, wParams);
                }
            });
    }
}
