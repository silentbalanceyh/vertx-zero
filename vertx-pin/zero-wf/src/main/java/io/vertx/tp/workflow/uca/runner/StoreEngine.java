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
import org.camunda.bpm.engine.RuntimeService;
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
     * Workflow Diagram by definition Key
     */
    @Override
    public Future<JsonObject> processByKey(final String definitionKey) {
        return Wf.processByKey(definitionKey)
            // Workflow Diagram and Task
            .compose(this::workflowNotStart);
    }

    /*
     * Workflow Diagram by definition Id
     */
    @Override
    public Future<JsonObject> processById(final String definitionId) {
        return Wf.processById(definitionId)
            // Workflow Diagram and Task
            .compose(this::workflowNotStart);
    }

    @Override
    public Future<JsonObject> processByTask(final String instanceId) {
        return this.instanceById(instanceId).compose(instance -> Wf.processById(instance.getProcessDefinitionId())
            .compose(definition -> this.workflowStart(definition, instance)));
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
    private Future<JsonObject> workflowStart(final ProcessDefinition definition, final ProcessInstance instance) {
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
    private Future<JsonObject> workflowNotStart(final ProcessDefinition definition) {
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
     * {
     *      "code": "???",
     *      "formKey": "???",
     *      "definitionId": "???",
     *      "definitionKey": "???",
     * }
     */
    private Future<JsonObject> formNonStart(final ProcessDefinition definition, final StartFormData startForm) {
        final String formKey = startForm.getFormKey();
        return Ux.future(Wf.formOut(formKey, definition.getId(), definition.getKey()));
    }

    /*
     * {
     *      "code": "???",
     *      "formKey": "???",
     *      "definitionId": "???",
     *      "definitionKey": "???",
     * }
     */
    private Future<JsonObject> formStart(final ProcessDefinition definition, final Task task) {
        final String formKey = task.getFormKey();
        return Ux.future(Wf.formOut(formKey, definition.getId(), definition.getKey()));
    }

    @Override
    public Future<JsonObject> formById(final String processId, final boolean isTask) {
        if (isTask) {
            // Task form, here definition Id is instance id
            // TaskForm: form by instance
            final EventOn eventOn = EventOn.get();
            return this.instanceById(processId).compose(instance -> Wf.processById(instance.getProcessDefinitionId())
                .compose(definition -> eventOn.taskActive(instance)
                    /*
                     * {
                     *      "code": "???",
                     *      "formKey": "???",
                     *      "definitionId": "???",
                     *      "definitionKey": "???",
                     * }
                     */
                    .compose(task -> this.formStart(definition, task))
                )
            );
        } else {
            // Task Not Started
            // StartForm: form by definition
            return Wf.processById(processId).compose(definition -> {
                final FormService formService = WfPin.camundaForm();
                final StartFormData startForm = formService.getStartFormData(processId);
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
                return this.formNonStart(definition, startForm);
            });
        }
    }

    @Override
    public Future<ProcessInstance> instanceById(final String instanceId) {
        if (Objects.isNull(instanceId)) {
            return Ux.future();
        } else {
            final RuntimeService service = WfPin.camundaRuntime();
            final ProcessInstance instance = service.createProcessInstanceQuery()
                .processInstanceId(instanceId).singleResult();
            return Ux.future(instance);
        }
    }
}
