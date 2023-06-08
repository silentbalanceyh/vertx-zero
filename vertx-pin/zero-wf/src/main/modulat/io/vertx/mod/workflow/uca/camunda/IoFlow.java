package io.vertx.mod.workflow.uca.camunda;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.refine.Wf;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.StartEvent;

import java.util.Set;

/**
 * S - Start Flow
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IoFlow extends AbstractIo<JsonObject> {

    // 「IoRuntime」ProcessDefinition before workflow start
    /*
     * Workflow Output
     * {
     *      "definitionId": "???",
     *      "definitionKey": "???",
     *      "bpmn": "???",
     *      "name": "???",
     *      "task": "???",
     *      "multiple": "???"
     * }
     */
    @Override
    public Future<JsonObject> start(final ProcessDefinition definition) {
        final JsonObject workflow = Wf.outBpmn(definition);

        // Capture the definition from BPMN
        final Io<StartEvent> ioStart = Io.ioEventStart();
        return Ux.future(definition.getId())
            // Workflow Fetching
            .compose(ioStart::child)
            .compose(starts -> ioStart.out(workflow, starts));
    }

    // 「IoRuntime」Instance when the workflow is running
    /*
     * Workflow Output
     * {
     *      "definitionId": "???",
     *      "definitionKey": "???",
     *      "bpmn": "???",
     *      "name": "???",
     *      "history": []
     * }
     */

    // 「IoRuntime」Instance when the workflow is finished
    /*
     * Workflow Output
     * {
     *      "definitionId": "???",
     *      "definitionKey": "???",
     *      "bpmn": "???",
     *      "name": "???",
     *      "history": []
     * }
     */
    @Override
    public Future<JsonObject> end(final HistoricProcessInstance instance) {
        /*
         * 1. ProcessDefinition
         * 2. HistoricProcessInstance
         */
        final ProcessDefinition definition = this.inProcess(instance.getProcessDefinitionId());

        final JsonObject workflow = Wf.outBpmn(definition);
        final Io<EndEvent> ioEnd = Io.ioEventEnd();
        final Io<Set<String>> ioHistory = Io.ioHistory();
        return Ux.future(definition.getId())
            // Workflow Fetching
            .compose(ioEnd::children)
            .compose(ends -> ioEnd.out(workflow, ends))

            // History Fetching
            .compose(nil -> ioHistory.end(instance))
            .compose(historySet -> ioHistory.out(workflow, historySet));
    }

    @Override
    public Future<JsonObject> run(final Task task) {
        final ProcessDefinition definition = this.inProcess(task.getProcessDefinitionId());
        final JsonObject workflow = Wf.outBpmn(definition);

        final ProcessInstance instance = this.inInstance(task.getProcessInstanceId());

        final Io<Set<String>> ioHistory = Io.ioHistory();
        final Io<Task> ioTask = Io.ioTask();
        return ioTask.out(workflow, task)
            .compose(nil -> ioHistory.run(instance))
            .compose(historySet -> {
                /*
                 * Remove current task for printing to avoid
                 * `success` and `active` here, because we'll draw ongoing process in this method
                 * it means that the system must distinguish history and active,
                 *
                 * 1. active is current
                 * 2. history should be finished
                 */
                historySet.remove(task.getTaskDefinitionKey());
                return ioHistory.out(workflow, historySet);
            });
    }

    @Override
    public Future<JsonObject> out(final JsonObject response, final JsonObject workflow) {
        if (Ut.isNotNil(workflow)) {
            response.put(KName.Flow.WORKFLOW, workflow);
        }
        return Ux.future(response);
    }
}
