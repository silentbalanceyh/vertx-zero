package io.vertx.tp.workflow.uca.runner;

import cn.zeroup.macrocosm.cv.WfPool;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface AidOn {

    static AidOn get() {
        return WfPool.CC_AID.pick(AidEngine::new);
    }

    String taskType(Task task);

    boolean isEnd(ProcessInstance instance);
}
