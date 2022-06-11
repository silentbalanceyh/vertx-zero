package io.vertx.tp.workflow.refine;

import cn.zeroup.macrocosm.cv.WfCv;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class WfCamunda {

    // ------------------- Other Output ------------------------
    static JsonObject bpmnOut(final ProcessDefinition definition) {
        final RepositoryService service = WfPin.camundaRepository();
        // Content Definition
        final BpmnModelInstance instance = service.getBpmnModelInstance(definition.getId());
        Objects.requireNonNull(instance);
        final String xml = Bpmn.convertToString(instance);
        // Response Json
        final JsonObject workflow = new JsonObject();
        workflow.put(KName.Flow.DEFINITION_ID, definition.getId());
        workflow.put(KName.Flow.DEFINITION_KEY, definition.getKey());
        workflow.put(KName.Flow.BPMN, xml);
        workflow.put(KName.NAME, definition.getName());
        return workflow;
    }

    static String eventName(final Task task) {
        if (Objects.isNull(task)) {
            return null;
        }
        final RepositoryService service = WfPin.camundaRepository();
        final BpmnModelInstance instance = service.getBpmnModelInstance(task.getProcessDefinitionId());
        final ModelElementInstance node = instance.getModelElementById(task.getTaskDefinitionKey());
        return node.getElementType().getTypeName();
    }

    static JsonObject taskOut(final JsonObject workflow, final Task task) {
        Objects.requireNonNull(task);
        workflow.put(KName.MULTIPLE, Boolean.FALSE);
        workflow.put(KName.Flow.TASK, task.getTaskDefinitionKey());
        workflow.put(KName.Flow.TASK_NAME, task.getName());
        // History Processing
        return workflow;
    }

    // --------------- Form Fetching ------------------
    static JsonObject formOut(final String formKey, final String definitionId, final String definitionKey) {
        Objects.requireNonNull(formKey);
        final String code = formKey.substring(formKey.lastIndexOf(Strings.COLON) + 1);
        // Build Form ConfigRunner parameters
        final JsonObject response = new JsonObject();
        response.put(KName.CODE, code);
        response.put(KName.Flow.FORM_KEY, formKey);
        response.put(KName.Flow.DEFINITION_KEY, definitionKey);
        response.put(KName.Flow.DEFINITION_ID, definitionId);
        return response;
    }

    static JsonObject formInput(final JsonObject form, final String sigma) {
        final String definition = form.getString(KName.Flow.DEFINITION_KEY);
        final JsonObject parameters = new JsonObject();
        final String code = form.getString(KName.CODE);
        final String configFile = WfCv.FOLDER_ROOT + "/" + definition + "/" + code + ".json";
        // Dynamic Processing
        if (Ut.ioExist(configFile)) {
            parameters.put(KName.DYNAMIC, Boolean.FALSE);
            parameters.put(KName.CODE, configFile);
        } else {
            parameters.put(KName.DYNAMIC, Boolean.TRUE);
            parameters.put(KName.CODE, code);
            parameters.put(KName.SIGMA, sigma);
        }
        return parameters;
    }
}
