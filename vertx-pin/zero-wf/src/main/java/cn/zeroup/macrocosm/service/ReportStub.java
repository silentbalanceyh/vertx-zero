package cn.zeroup.macrocosm.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface ReportStub {
    /*
     * W_TODO JOIN W_TICKET
     */
    Future<JsonObject> fetchQueue(JsonObject condition);

    /*
    * Fetch Activity
    * */
    Future<JsonArray> fetchActivity(String key, String modelKey);
    Future<JsonArray> fetchActivityByUser(String key, String modelKey, String userId);
}
