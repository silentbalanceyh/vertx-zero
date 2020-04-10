package cn.vertxup.ui.service;

import io.vertx.core.Future;
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
}
