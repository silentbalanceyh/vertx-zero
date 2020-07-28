package cn.vertxup.ambient.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface ModelStub {
    /*
     * Get module by
     * 1) appId: application key of primary key
     * 2) uri: module uri for entry here
     */
    Future<JsonObject> fetchModule(String appId, String entry);
}
