package cn.vertxup.ambient.service.application;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface MenuStub {
    /*
     * Fetch menu by id
     * Get menus by : appId = {xxx}
     */
    Future<JsonArray> fetchByApp(String appId);
}
