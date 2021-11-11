package io.vertx.tp.workflow.uca.runner;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._404ProcessMissingException;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class StoreEngine implements StoreOn {
    @Override
    public Future<JsonObject> processByKey(final String code) {
        final RepositoryService service = WfPin.camundaRepository();
        final ProcessDefinition definition = service.createProcessDefinitionQuery()
            .processDefinitionKey(code).singleResult();
        return this.processData(definition, code);
    }

    private Future<JsonObject> processData(final ProcessDefinition definition, final String code) {
        final RepositoryService service = WfPin.camundaRepository();
        if (Objects.isNull(definition)) {
            // Error
            return Ux.thenError(_404ProcessMissingException.class, this.getClass(), code);
        }
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
        return Ux.future(workflow);
    }

    @Override
    public Future<JsonObject> processById(final String definitionId) {
        final RepositoryService service = WfPin.camundaRepository();
        final ProcessDefinition definition = service.getProcessDefinition(definitionId);
        return this.processData(definition, definitionId);
    }

    @Override
    public Future<JsonObject> fetchForm(final String definitionId, final boolean isTask) {
        final RepositoryService service = WfPin.camundaRepository();
        final ProcessDefinition definition = service.getProcessDefinition(definitionId);
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
    }
}
