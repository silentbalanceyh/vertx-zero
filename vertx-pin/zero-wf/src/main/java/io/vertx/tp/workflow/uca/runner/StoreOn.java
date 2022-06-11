package io.vertx.tp.workflow.uca.runner;

import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface StoreOn {
    static StoreOn get() {
        return WfPool.CC_STORE.pick(StoreEngine::new);
    }

    /*
     * {
     *      "definitionId": "xxx",
     *      "code": "workflow code",
     *      "bpmn": "xml definition"
     * }
     */

    Future<JsonObject> workflowGet(ProcessDefinition definition);

    Future<JsonObject> workflowGet(ProcessDefinition definition, ProcessInstance instance);

    Future<JsonObject> workflowGet(ProcessDefinition definition, HistoricProcessInstance instance);

    Future<JsonObject> workflowGet(ProcessInstance instance);

    Future<JsonObject> workflowGet(HistoricProcessInstance instance);

    /*
     * {
     *      "code": "the last one",
     *      "formKey": "the original form Key",
     *      "fields": {
     *      }
     * }
     */
    Future<JsonObject> formGet(ProcessDefinition definition);

    Future<JsonObject> formGet(ProcessDefinition definition, ProcessInstance instance);
}
