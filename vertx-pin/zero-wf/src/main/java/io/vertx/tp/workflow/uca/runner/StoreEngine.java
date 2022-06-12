package io.vertx.tp.workflow.uca.runner;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.camunda.Io;
import io.vertx.up.atom.Refer;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.instance.EndEvent;

import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class StoreEngine implements StoreOn {

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
    public Future<JsonObject> workflowGet(final ProcessDefinition definition, final HistoricProcessInstance instance) {
        final JsonObject workflow = Wf.bpmnOut(definition);
        final Io<EndEvent> io = Io.ioEventEnd();
        final Io<Set<String>> ioHistory = Io.ioHistory();
        return io.children(definition.getId())
            /*
             * {
             *      "task": "???",
             *      "multiple": "???"
             * }
             */
            .compose(ends -> io.out(workflow, ends))
            .compose(response -> ioHistory.end(instance.getId()))
            .compose(history -> ioHistory.out(workflow, history));
    }

    @Override
    public Future<JsonObject> workflowGet(final ProcessInstance instance) {
        return null;
    }

    @Override
    public Future<JsonObject> workflowGet(final HistoricProcessInstance instance) {
        return null;
    }

    /*
     * Workflow Output
     * {
     *      "definitionId": "???",
     *      "definitionKey": "???",
     *      "bpmn": "???",
     *      "name": "???",
     *      "task": "???",
     *      "multiple": "???",
     *      "history": []
     * }
     */
    @Override
    public Future<JsonObject> workflowGet(final ProcessDefinition definition, final ProcessInstance instance) {
        final JsonObject workflow = Wf.bpmnOut(definition);
        final EventOn eventOn = EventOn.get();
        final Refer responseRef = new Refer();
        final Refer taskRef = new Refer();
        final Io<Set<String>> ioHistory = Io.ioHistory();
        return eventOn.taskOldActive(instance)
            .compose(taskRef::future)
            /*
             * {
             *      "task": "???",
             *      "multiple": "???"
             * }
             */
            .compose(task -> Ux.future(Wf.taskOut(workflow, task)))
            .compose(responseRef::future)
            .compose(nil -> ioHistory.run(instance.getId()))
            .compose(history -> {
                /*
                 * Remove current task for printing to avoid
                 * `success` and `active` here, because we'll draw ongoing process in this method
                 * it means that the system must distinguish history and active,
                 *
                 * 1. active is current
                 * 2. history should be finished
                 */
                final Task task = taskRef.get();
                history.remove(task.getTaskDefinitionKey());
                /*
                 * {
                 *     "history": "???"
                 * }
                 */
                final JsonArray historyA = Ut.toJArray(history);
                final JsonObject response = responseRef.get();
                return Ux.future(response.put(KName.HISTORY, historyA));
            });
    }

    // Task Not Started
    // StartForm: form by definition
    @Override
    public Future<JsonObject> formGet(final ProcessDefinition definition) {
        final FormService formService = WfPin.camundaForm();
        final StartFormData startForm = formService.getStartFormData(definition.getId());
        Objects.requireNonNull(startForm);
        // substring
        /*
         * {
         *      "code": "???",
         *      "formKey": "???",
         *      "definitionId": "???",
         *      "definitionKey": "???",
         * }
         */
        final String formKey = startForm.getFormKey();
        return Ux.future(Wf.formOut(formKey, definition.getId(), definition.getKey()));
        // return this.formNonStart(definition, startForm);
    }

    // Task form, here definition Id is instance id
    // TaskForm: form by instance
    @Override
    public Future<JsonObject> formGet(final ProcessDefinition definition, final ProcessInstance instance) {
        final EventOn eventOn = EventOn.get();
        return eventOn.taskOldActive(instance)
            /*
             * {
             *      "code": "???",
             *      "formKey": "???",
             *      "definitionId": "???",
             *      "definitionKey": "???",
             * }
             */
            .compose(task -> {
                final String formKey = task.getFormKey();
                return Ux.future(Wf.formOut(formKey, definition.getId(), definition.getKey()));
            });
    }
}
