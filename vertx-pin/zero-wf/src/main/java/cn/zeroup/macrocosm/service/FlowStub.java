package cn.zeroup.macrocosm.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface FlowStub {

    Future<JsonObject> fetchFlow(String definitionKey, String sigma);

    /*
     * Process by id ( unique )
     */
    Future<JsonObject> fetchForm(ProcessDefinition definition, String sigma);

    Future<JsonObject> fetchFormEnd(ProcessDefinition definition, HistoricProcessInstance instance, String sigma);

    /*
     * Process by instance id ( unique )
     */
    Future<JsonObject> fetchForm(ProcessDefinition definition, ProcessInstance instance, String sigma);
}
