package io.vertx.tp.workflow.refine;

import cn.zeroup.macrocosm.cv.em.PassWay;
import io.vertx.core.json.JsonArray;
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
import java.util.Set;

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

    // ------------------- Gateway Type Analyzing ------------------------
    /*
     * PassWay Input Data
     * 1: 1
     * {
     *     "toUser": "user1"
     * }
     *
     * n: 1
     * {
     *     "toUser": {
     *         "type1": "user1",
     *         "type2": "user2",
     *         "type3": "user3",
     *         "...":   "...",
     *         "typeN": "userN"
     *     }
     * }
     *
     * 1: n
     * {
     *     "toUser": [
     *         "user1",
     *         "user2",
     *         "user3",
     *         "...",
     *         "userN"
     *     ]
     * }
     *
     * n: n
     * {
     *     "toUser": {
     *         "type1": [
     *              "user1",
     *              "user2",
     *              "..."
     *         ],
     *         "type2": [
     *              "user3",
     *              "user4",
     *              "...",
     *              "userY"
     *         ],
     *         "...": [
     *              "...",
     *              "userN"
     *         ],
     *         "typeN": [
     *              "user2",
     *              "user3",
     *              "...",
     *              "userX"
     *         ],
     *     }
     * }
     */
    static PassWay inGateway(final JsonObject requestJ) {
        // toUser field extraction
        final JsonObject requestData = Ut.valueJObject(requestJ);
        final Object toUser = requestData.getValue(KName.Flow.Auditor.TO_USER);
        if (toUser instanceof String) {
            // String
            return PassWay.Standard;
        } else if (toUser instanceof JsonArray) {
            // JsonArray
            return PassWay.Multi;
        } else if (toUser instanceof JsonObject) {
            // JsonObject
            /*
             * - Fork/Join:  All String
             * - Grid:       All JsonObject
             */
            final JsonObject toUserJ = ((JsonObject) toUser);
            final Set<String> typeSet = toUserJ.fieldNames();
            final long strCount = typeSet.stream()
                .filter(field -> (toUserJ.getValue(field) instanceof String))
                .count();
            if (typeSet.size() == strCount) {
                // Fork/Join
                return PassWay.Fork;
            } else {
                // Grid
                return PassWay.Grid;
            }
        } else {
            // Undermine
            return null;
        }
    }

    // ------------------- Name Event ------------------------
    static String nameEvent(final Task task) {
        if (Objects.isNull(task)) {
            return null;
        }
        final RepositoryService service = WfPin.camundaRepository();
        final BpmnModelInstance instance = service.getBpmnModelInstance(task.getProcessDefinitionId());
        final ModelElementInstance node = instance.getModelElementById(task.getTaskDefinitionKey());
        return node.getElementType().getTypeName();
    }
}
