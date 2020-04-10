package cn.vertxup.ui.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface TplStub {
    /*
     * Cached layout information here to stored
     */
    Future<JsonObject> fetchLayout(String layoutId);
}
