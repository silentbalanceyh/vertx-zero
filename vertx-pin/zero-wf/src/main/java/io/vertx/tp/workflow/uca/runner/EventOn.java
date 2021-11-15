package io.vertx.tp.workflow.uca.runner;

import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.core.Future;
import io.vertx.up.fn.Fn;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.instance.StartEvent;

import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface EventOn {

    static EventOn get() {
        return Fn.poolThread(WfPool.POOL_EVENT, EventEngine::new);
    }

    /*
     * Event fetch part from Process
     */
    Future<Set<StartEvent>> startSet(String definitionId);

    Future<StartEvent> start(String definitionId);

    /*
     * Event Id from ProcessInstance
     */
    Future<Task> task(ProcessInstance instance);
}
