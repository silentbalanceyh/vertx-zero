package io.vertx.mod.workflow.refine;

import cn.vertxup.workflow.cv.em.PassWay;
import io.horizon.uca.log.Log;
import io.horizon.uca.log.LogModule;
import io.vertx.core.json.JsonObject;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;

import java.util.List;

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
    public static JsonObject outBpmn(final ProcessDefinition definition) {
        return WfFlow.outBpmn(definition);
    }

    public static JsonObject outLinkage(final JsonObject linkageJ) {
        return WfFlow.outLinkage(linkageJ);
    }

    public static PassWay inGateway(final JsonObject requestJ) {
        return WfFlow.inGateway(requestJ);
    }

    public static String nameEvent(final Task task) {
        return WfFlow.nameEvent(task);
    }

    public static List<Task> taskNext(final Task task, final List<Task> source) {
        return WfFlow.taskNext(task, source);
    }

    public interface LOG {
        String MODULE = "Ροή εργασίας";

        LogModule Init = Log.modulat(MODULE).program("Init");
        LogModule Queue = Log.modulat(MODULE).program("Queue");
        LogModule Deploy = Log.modulat(MODULE).program("Deploy");
        LogModule Move = Log.modulat(MODULE).program("Move");
        LogModule Plugin = Log.modulat(MODULE).program("Infusion");
        LogModule Web = Log.modulat(MODULE).program("Web");
    }
}
