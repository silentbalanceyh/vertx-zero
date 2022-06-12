package io.vertx.tp.workflow.refine;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.up.eon.KName;
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
}
