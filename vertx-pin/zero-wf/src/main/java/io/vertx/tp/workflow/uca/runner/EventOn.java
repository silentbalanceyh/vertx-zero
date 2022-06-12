package io.vertx.tp.workflow.uca.runner;

import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.core.Future;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface EventOn {

    static EventOn get() {
        return WfPool.CC_EVENT.pick(EventEngine::new);
    }

    /*
     * Event Id from ProcessInstance
     */
    @Deprecated
    Future<Task> taskOldActive(ProcessInstance instance);

    @Deprecated
    Future<Task> taskOldActive(String taskId);

    @Deprecated
    Future<Task> taskOldSmart(ProcessInstance instance, String taskId);
}
