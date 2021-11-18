package io.vertx.tp.workflow.uca.runner;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.atom.Refer;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.util.Objects;

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
     *      "task": "???",
     *      "multiple": "???"
     * }
     */
    @Override
    public Future<JsonObject> workflowByDefinition(final ProcessDefinition definition) {
        final JsonObject workflow = Wf.bpmnOut(definition);
        final EventOn eventOn = EventOn.get();
        return eventOn.startSet(definition.getId())
            /*
             * {
             *      "task": "???",
             *      "multiple": "???"
             * }
             */
            .compose(starts -> Ux.future(Wf.taskOut(workflow, starts)));
    }

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
    public Future<JsonObject> workflowByInstance(final ProcessDefinition definition, final ProcessInstance instance) {
        final JsonObject workflow = Wf.bpmnOut(definition);
        final EventOn eventOn = EventOn.get();
        final Refer responseRef = new Refer();
        final Refer taskRef = new Refer();
        return eventOn.taskActive(instance)
            .compose(taskRef::future)
            /*
             * {
             *      "task": "???",
             *      "multiple": "???"
             * }
             */
            .compose(task -> Ux.future(Wf.taskOut(workflow, task)))
            .compose(responseRef::future)
            .compose(nil -> eventOn.taskHistory(instance))
            .compose(history -> {
                /*
                 * Remove current task for printing
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
    public Future<JsonObject> formByDefinition(final ProcessDefinition definition) {
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
    public Future<JsonObject> formByInstance(final ProcessDefinition definition, final ProcessInstance instance) {
        final EventOn eventOn = EventOn.get();
        return eventOn.taskActive(instance)
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
