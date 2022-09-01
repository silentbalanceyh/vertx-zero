package cn.vertxup.workflow.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface FlowStub {

    Future<JsonObject> fetchFlow(String definitionKey, String sigma);

    /*
     * Fetch three forms by different phase
     * - (ProcessDefinition, sigma)         - Start Form
     * - (HistoricProcessInstance, sigma)   - End Form
     * - (ProcessInstance, Task, sigma)     - Run Form
     */
    Future<JsonObject> fetchForm(ProcessDefinition definition, String sigma);

    Future<JsonObject> fetchForm(HistoricProcessInstance instance, String sigma);

    /*
     * Process by instance id ( unique )
     */
    Future<JsonObject> fetchForm(ProcessInstance instance, Task task, String sigma);
}
