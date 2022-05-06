package io.vertx.tp.workflow.uca.runner;

import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.core.Future;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.StartEvent;

import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface EventOn {

    static EventOn get() {
        return WfPool.CC_EVENT.pick(EventEngine::new);
    }

    /*
     * Event fetch part from Process
     */
    Future<Set<StartEvent>> startSet(String definitionId);

    Future<StartEvent> start(String definitionId);

    Future<Set<EndEvent>> endSet(String definitionId);

    Future<EndEvent> end(String definitionId);

    /*
     * Event Id from ProcessInstance
     */
    Future<Task> taskActive(ProcessInstance instance);

    Future<Task> taskActive(String taskId);

    Future<Task> taskSmart(ProcessInstance instance, String taskId);

    /*
     * Task History
     */
    Future<Set<String>> taskHistory(ProcessInstance instance);

    Future<Set<String>> taskHistory(HistoricProcessInstance instance);
}
