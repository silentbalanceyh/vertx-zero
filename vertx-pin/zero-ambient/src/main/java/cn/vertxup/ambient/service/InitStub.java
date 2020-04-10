package cn.vertxup.ambient.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface InitStub {
    /*
     * Initializing X_APP
     *
     */
    Future<JsonObject> initApp(String appId, JsonObject data);

    /*
     * Initializing on existing X_APP
     * name as parameter instead of app key, name is unique field in X_APP
     */
    Future<JsonObject> initExtension(String appName);

    /*
     * Prerequisite Interface extension.
     */
    Future<JsonObject> prerequisite(String appName);
}
