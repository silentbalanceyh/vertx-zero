package io.vertx.tp.workflow.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.runtime.WProcess;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.up.log.Annal;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Wf {

    /*
     * Workflow Output
     * {
     *      "definitionId": "Process Definition Id",
     *      "definitionKey": "Process Definition Key",
     *      "bpmn": "Xml format of BPMN 2.0 diagram",
     *      "name": "Process Definition Name"
     * }
     */
    public static JsonObject bpmnOut(final ProcessDefinition definition) {
        return WfCamunda.bpmnOut(definition);
    }

    public static String eventName(final Task task) {
        return WfCamunda.eventName(task);
    }

    /*
     * {
     *      "task": "Event name of task, event id",
     *      "multiple": "Whether there are more than one task",
     *      "history": []
     * }
     */
    public static JsonObject taskOut(final JsonObject workflow, final Task task) {
        return WfCamunda.taskOut(workflow, task);
    }

    // Fetch WProcess
    public static Future<WProcess> process(final WRequest request) {
        return WfFlow.process(request);
    }

    public static JsonObject processLinkage(final JsonObject linkageJ) {
        return WfFlow.processLinkage(linkageJ);
    }

    // BiFunction on ProcessDefinition / ProcessInstance

    public static class Log {
        public static void infoInit(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            WfLog.info(logger, "Init", message, args);
        }

        public static void initQueue(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            WfLog.info(logger, "Queue", message, args);
        }

        public static void debugInit(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            WfLog.debug(logger, "Init", message, args);
        }

        public static void infoDeploy(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            WfLog.info(logger, "Deploy", message, args);
        }

        public static void warnDeploy(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            WfLog.warn(logger, "Deploy", message, args);
        }

        public static void warnMove(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            WfLog.warn(logger, "Move", message, args);
        }

        public static void debugDeploy(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            WfLog.debug(logger, "Deploy", message, args);
        }

        public static void infoMove(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            WfLog.info(logger, "Move", message, args);
        }

        public static void infoWeb(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            WfLog.info(logger, "Web", message, args);
        }

        public static void debugMove(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            WfLog.debug(logger, "Move", message, args);
        }
    }
}
