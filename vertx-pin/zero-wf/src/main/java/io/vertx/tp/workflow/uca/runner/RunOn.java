package io.vertx.tp.workflow.uca.runner;

import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.WMove;
import io.vertx.up.fn.Fn;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface RunOn {

    static RunOn get() {
        return Fn.poolThread(WfPool.POOL_PROC, RunEngine::new);
    }

    // Start
    Future<ProcessInstance> startAsync(String definitionKey, WMove move);

    // Run
    Future<ProcessInstance> moveAsync(ProcessInstance instance, WMove move);

    // Stop
    Future<Boolean> stopAsync(ProcessInstance instance);
}
