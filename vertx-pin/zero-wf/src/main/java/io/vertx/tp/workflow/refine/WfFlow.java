package io.vertx.tp.workflow.refine;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.up.eon.KName;
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
class WfFlow {

    // ------------------- Other Output ------------------------
    static JsonObject outBpmn(final ProcessDefinition definition) {
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

    static String nameEvent(final Task task) {
        if (Objects.isNull(task)) {
            return null;
        }
        final RepositoryService service = WfPin.camundaRepository();
        final BpmnModelInstance instance = service.getBpmnModelInstance(task.getProcessDefinitionId());
        final ModelElementInstance node = instance.getModelElementById(task.getTaskDefinitionKey());
        return node.getElementType().getTypeName();
    }

    static JsonObject outLinkage(final JsonObject linkageJ) {
        final JsonObject parsed = new JsonObject();
        linkageJ.fieldNames().forEach(field -> {
            final Object value = linkageJ.getValue(field);
            /*
             * Secondary format for
             * field1: path1
             * field1: path2
             */
            JsonObject json = null;
            if (value instanceof String) {
                json = Ut.ioJObject(value.toString());
            } else if (value instanceof JsonObject) {
                json = (JsonObject) value;
            }
            if (Ut.notNil(json)) {
                assert json != null;
                parsed.put(field, json.copy());
            }
        });
        return parsed;
    }
}
