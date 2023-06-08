package cn.vertxup.ui.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ui.cv.em.ControlType;

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
     * Fetch control based on UI_VISITOR
     */
    Future<JsonObject> fetchControl(ControlType controlType, JsonObject params);
}
