package cn.vertxup.ui.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface ListStub {

    String FIELD_OPTIONS_AJAX = "optionsAjax";
    String FIELD_OPTIONS_SUBMIT = "optionsSubmit";
    String FIELD_OPTIONS = "options";
    String FIELD_V_SEGMENT = "vSegment";
    String FIELD_V_QUERY = "vQuery";
    String FIELD_V_SEARCH = "vSearch";
    String FIELD_V_TABLE = "vTable";

    /*
     * By id
     */
    Future<JsonObject> fetchById(String listId);

    /*
     * By identifier & sigma
     */
    Future<JsonArray> fetchByIdentifier(String identifier, String sigma);

    Future<JsonArray> fetchQr(JsonObject condition);
}
