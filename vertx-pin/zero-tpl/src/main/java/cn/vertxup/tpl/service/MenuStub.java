package cn.vertxup.tpl.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface MenuStub {

    /*
     * Fetch my menu
     */
    Future<JsonArray> fetchMy(JsonObject condition);

    /*
     * Save my menu definition
     */
    Future<JsonArray> saveMy(JsonObject condition, JsonArray data);
}
