package cn.vertxup.ui.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/*
 * Page Service for configuration
 */
public interface PageStub {
    /*
     * Fetch page by: app + module + page
     */
    Future<JsonObject> fetchAmp(String sigma, JsonObject params);

    /*
     * Cached layout information here to stored
     */
    Future<JsonObject> fetchLayout(String layoutId);
}
