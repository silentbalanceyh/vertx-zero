package cn.vertxup.workflow.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface TaskStub {
    /*
     * W_TODO JOIN W_TICKET
     */
    Future<JsonObject> fetchQueue(JsonObject condition);

    Future<JsonObject> fetchHistory(JsonObject condition);

    /*
     * Fetch detail information
     */
    Future<JsonObject> readPending(String key, String userId);

    Future<JsonObject> readFinished(String key);
}
