package io.vertx.tp.workflow.uca.runner;

import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface StoreOn {
    static StoreOn get() {
        return Fn.poolThread(WfPool.POOL_STORE, StoreEngine::new);
    }

    /*
     * {
     *      "definitionId": "xxx",
     *      "code": "workflow code",
     *      "bpmn": "xml definition"
     * }
     */

    Future<JsonObject> workflowByDefinition(ProcessDefinition definition);

    Future<JsonObject> workflowByInstance(ProcessDefinition definition, ProcessInstance instance);

    /*
     * {
     *      "code": "the last one",
     *      "formKey": "the original form Key",
     *      "fields": {
     *      }
     * }
     */
    Future<JsonObject> formByDefinition(ProcessDefinition definition);

    Future<JsonObject> formByInstance(ProcessDefinition definition, ProcessInstance instance);

}
