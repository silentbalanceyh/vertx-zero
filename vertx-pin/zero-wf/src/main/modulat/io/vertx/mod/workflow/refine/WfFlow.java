package io.vertx.mod.workflow.refine;

import cn.vertxup.workflow.cv.em.PassWay;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.init.WfPin;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.Outgoing;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.*;
import java.util.stream.Collectors;

import static io.vertx.mod.workflow.refine.Wf.LOG;

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
            if (Ut.isNotNil(json)) {
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
        final Object toUser = requestData.getValue(KName.Auditor.TO_USER);
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

    static List<Task> taskNext(final Task task, final List<Task> source) {
        Objects.requireNonNull(task);
        final RepositoryService service = WfPin.camundaRepository();
        final BpmnModelInstance instance = service.getBpmnModelInstance(task.getProcessDefinitionId());
        final ModelElementInstance node = instance.getModelElementById(task.getTaskDefinitionKey());
        final Set<String> nextKeys = taskSearch(node, instance);
        LOG.Move.info(WfFlow.class, "[Outgoing] Keys = {0}", Ut.fromJoin(nextKeys));
        return source.stream()
            .filter(taskNext -> nextKeys.contains(taskNext.getTaskDefinitionKey()))
            .collect(Collectors.toList());
    }

    /*
     * The method is recursion calling on BPMN, here are some situations
     * 1. The objective will be: Find the next all UserTask
     * 2. When the system met gateway node, continue to find the task here
     */
    private static Set<String> taskSearch(final ModelElementInstance nodeTask, final BpmnModelInstance instance) {
        // Find all `outgoing` that belong to `nodeTask`
        final Collection<Outgoing> outgoing = nodeTask.getChildElementsByType(Outgoing.class);
        final Set<String> keys = new HashSet<>();
        outgoing.forEach(child -> {
            final ModelElementInstance sequence = instance.getModelElementById(child.getTextContent());
            // SequenceFlow
            if (sequence instanceof SequenceFlow) {
                final FlowNode target = ((SequenceFlow) sequence).getTarget();
                final ModelElementInstance found = instance.getModelElementById(target.getId());
                if (found instanceof UserTask) {
                    // Task -> Task
                    keys.add(target.getId());
                } else {
                    // Task -> Gateway -> Task
                    final Set<String> continueSearch = taskSearch(found, instance);
                    keys.addAll(continueSearch);
                }
            }
        });
        return keys;
    }
}
