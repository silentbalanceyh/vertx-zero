package cn.vertxup.ui.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface FormStub {
    /*
     * By id
     */
    Future<JsonObject> fetchById(String formId);

    /*
     * By code & sigma
     */
    Future<JsonObject> fetchByCode(String code, String sigma);

    /*
     * By identifier & sigma
     */
    Future<JsonArray> fetchByIdentifier(String identifier, String sigma);

    /*
     * update
     */
    Future<JsonObject> update(String key, JsonObject data);

    /*
     * delete
     */
    Future<Boolean> delete(String key);
}
