package cn.vertxup.ui.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface ControlStub {
    /*
     * Fetch controls by pageId
     */
    Future<JsonArray> fetchControls(String pageId);

    /*
     * Fetch control by id
     */
    Future<JsonObject> fetchById(String control);

    /*
     * Fetch ops by control
     */
    Future<JsonArray> fetchOps(String control);
}
