package io.vertx.tp.workflow.refine;

import cn.zeroup.macrocosm.cv.WfCv;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._404ProcessMissingException;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.StartEvent;

import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class WfFlow {

    // --------------- Process Fetching ------------------
    static Future<ProcessDefinition> processByKey(final String definitionKey) {
        final RepositoryService service = WfPin.camundaRepository();
        final ProcessDefinition definition = service.createProcessDefinitionQuery()
            // New Version for each
            .latestVersion().processDefinitionKey(definitionKey).singleResult();
        return processInternal(definition, definitionKey);
    }

    static Future<ProcessDefinition> processById(final String definitionId) {
        final RepositoryService service = WfPin.camundaRepository();
        final ProcessDefinition definition = service.getProcessDefinition(definitionId);
        return processInternal(definition, definitionId);
    }

    private static Future<ProcessDefinition> processInternal(final ProcessDefinition definition, final String key) {
        if (Objects.isNull(definition)) {
            // Error
            return Ux.thenError(_404ProcessMissingException.class, WfFlow.class, key);
        } else {
            return Ux.future(definition);
        }
    }

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

    static JsonObject taskOut(final JsonObject workflow, final Set<StartEvent> starts) {
        if (1 == starts.size()) {
            final StartEvent event = starts.iterator().next();
            workflow.put(KName.Flow.TASK, event.getId());
            workflow.put(KName.MULTIPLE, Boolean.FALSE);
        } else {
            final JsonObject startMap = new JsonObject();
            starts.forEach(start -> startMap.put(start.getId(), start.getName()));
            workflow.put(KName.Flow.TASK, startMap);
            workflow.put(KName.MULTIPLE, Boolean.TRUE);
        }
        return workflow;
    }

    static JsonObject taskOut(final JsonObject workflow, final Task task) {
        Objects.requireNonNull(task);
        workflow.put(KName.MULTIPLE, Boolean.FALSE);
        workflow.put(KName.Flow.TASK, task.getTaskDefinitionKey());
        // History Processing
        return workflow;
    }

    // --------------- Form Fetching ------------------
    static JsonObject formOut(final String formKey, final String definitionId, final String definitionKey) {
        Objects.requireNonNull(formKey);
        final String code = formKey.substring(formKey.lastIndexOf(Strings.COLON) + 1);
        // Build Form Configuration parameters
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
        final String configFile = WfCv.ROOT_FOLDER + "/" + definition + "/" + code + ".json";
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
