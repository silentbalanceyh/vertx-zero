package io.vertx.tp.workflow.uca.camunda;

import cn.zeroup.macrocosm.cv.WfPool;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.runtime.WMove;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface RunOn {

    static RunOn get() {
        return WfPool.CC_RUN.pick(RunEngine::new);
    }

    // Start
    Future<ProcessInstance> startAsync(String definitionKey, WMove move);

    // Run
    Future<ProcessInstance> moveAsync(ProcessInstance instance, WMove move);

    // Stop
    Future<Boolean> stopAsync(ProcessInstance instance, TodoStatus status);

    // ---------------------- Running Checking -----------------------

}
