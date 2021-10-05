package cn.vertxup.rbac.service.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;


public interface ResourceStub {
    /**
     * Fetch resource + action by key
     */
    Future<JsonObject> fetchResource(String resourceId);

    /**
     * create resource + action
     */
    Future<JsonObject> createResource(JsonObject params);

    /**
     * Update resource + action
     */
    Future<JsonObject> updateResource(String resourceId, JsonObject params);

    /**
     * delete resource + action
     */
    Future<Boolean> deleteResource(String resourceId);
}
