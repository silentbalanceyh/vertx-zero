package cn.vertxup.ambient.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface ModelStub {
    /*
     * Get module by
     * 1) appId: application key of primary key
     * 2) uri: module uri for entry here
     */
    Future<JsonObject> fetchModule(String appId, String entry);

    /*
     * Get identifiers based sigma
     * 1) sigma: this is uniform field to distinguish
     * 2) This method will call channel to extract identifiers
     */
    Future<JsonArray> fetchModels(String sigma);

    /*
     * Fetch attributes cross
     * 1) Static models defined in environment
     * 2) Dynamic models defined in X_MODEL
     */
    Future<JsonArray> fetchAttrs(String identifier, String sigma);
}
