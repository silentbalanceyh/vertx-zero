package cn.zeroup.macrocosm.service;

import io.vertx.core.Future;
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
//    Future<JsonObject> fetchActivity(JsonObject condition);

    Future<JsonObject> fetchActivity(String key, User user);
    Future<JsonObject> fetchUserByActivity(String id);

    /*
     * Fetch Assets
     * */
    Future<JsonObject> fetchAssets(JsonObject condition);
}
