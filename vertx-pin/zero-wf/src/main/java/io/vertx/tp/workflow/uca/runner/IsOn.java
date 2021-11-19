package io.vertx.tp.workflow.uca.runner;

import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.up.fn.Fn;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface IsOn {

    static IsOn get() {
        return Fn.poolThread(WfPool.POOL_IS, IsEngine::new);
    }

    boolean isUserEvent(Task task);

    boolean isEnd(ProcessInstance instance);
}
