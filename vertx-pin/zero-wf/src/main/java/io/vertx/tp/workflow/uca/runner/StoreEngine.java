package io.vertx.tp.workflow.uca.runner;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.StartEvent;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class StoreEngine implements StoreOn {
    @Override
    public Future<JsonObject> processByKey(final String code) {
        return Wf.processByKey(code).compose(this::processData);
    }

    private Future<JsonObject> processData(final ProcessDefinition definition) {
        final RepositoryService service = WfPin.camundaRepository();
        // Content Definition
        final BpmnModelInstance instance = service.getBpmnModelInstance(definition.getId());
        Objects.requireNonNull(instance);
        final String xml = Bpmn.convertToString(instance);
        // Response Json
        final JsonObject workflow = new JsonObject();
        workflow.put(KName.Flow.DEFINITION_ID, definition.getId());
        workflow.put(KName.CODE, definition.getKey());
        workflow.put(KName.Flow.BPMN, xml);
        workflow.put(KName.NAME, definition.getName());
        // Start Event
        final EventOn eventOn = EventOn.get();
        return eventOn.startSet(definition.getId()).compose(starts -> {
            if (1 == starts.size()) {
                final StartEvent event = starts.iterator().next();
                workflow.put(KName.Flow.EVENT_START, event.getId());
                workflow.put(KName.Flow.MULTI_START, Boolean.FALSE);
            } else {
                final JsonObject startMap = new JsonObject();
                starts.forEach(start -> startMap.put(start.getId(), start.getName()));
                workflow.put(KName.Flow.EVENT_START, startMap);
                workflow.put(KName.Flow.MULTI_START, Boolean.TRUE);
            }
            return Ux.future(workflow);
        });
    }

    @Override
    public Future<JsonObject> processById(final String definitionId) {
        return Wf.processById(definitionId).compose(this::processData);
    }

    @Override
    public Future<JsonObject> formById(final String definitionId, final boolean isTask) {
        return Wf.processById(definitionId).compose(definition -> {
            if (isTask) {
                // Started
                return null;
            } else {
                // Not Started
                final FormService formService = WfPin.camundaForm();
                final StartFormData startForm = formService.getStartFormData(definitionId);
                Objects.requireNonNull(startForm);
                // substring
                final String formKey = startForm.getFormKey();
                Objects.requireNonNull(formKey);
                final String code = formKey.substring(formKey.lastIndexOf(Strings.COLON) + 1);
                // Build Form Configuration parameters
                final JsonObject response = new JsonObject();
                response.put(KName.CODE, code);
                response.put(KName.Flow.FORM_KEY, formKey);
                response.put(KName.Flow.DEFINITION_KEY, definition.getKey());
                return Ux.future(response);
            }
        });
    }

    @Override
    public Future<ProcessInstance> instanceById(final String definitionId) {
        final RuntimeService service = WfPin.camundaRuntime();
        final ProcessInstance instance = service.createProcessInstanceQuery()
            .processInstanceId(definitionId).singleResult();
        return Ux.future(instance);
    }
}
