package cn.vertxup.ambient.service.application;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface AppStub {
    /*
     * Before initialized app, fetchByName app data from system.
     */
    Future<JsonObject> fetchByName(String name);

    /*
     * Get application by: appId = {xxx}
     */
    Future<JsonObject> fetchById(String appId);

    /*
     * Get data source by: appId = {xxx}
     * Unique for each app
     */
    Future<JsonObject> fetchSource(String appId);
}
