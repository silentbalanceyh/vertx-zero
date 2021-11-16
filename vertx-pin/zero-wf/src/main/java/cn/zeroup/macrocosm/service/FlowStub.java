package cn.zeroup.macrocosm.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface FlowStub {

    Future<JsonObject> fetchFlow(String code, String sigma);

    /*
     * Process by id ( unique )
     */
    Future<JsonObject> fetchFormStart(String definitionId, String sigma);

    /*
     * Process by instance id ( unique )
     */
    Future<JsonObject> fetchFormTask(String instanceId, String sigma);
}
