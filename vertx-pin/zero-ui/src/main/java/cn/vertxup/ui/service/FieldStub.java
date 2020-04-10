package cn.vertxup.ui.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

public interface FieldStub {

    String OPTION_JSX = "optionJsx";
    String OPTION_CONFIG = "optionConfig";
    String OPTION_ITEM = "optionItem";

    /*
     * Fetch all fields
     */
    Future<JsonArray> fetchUi(String formId);
}
