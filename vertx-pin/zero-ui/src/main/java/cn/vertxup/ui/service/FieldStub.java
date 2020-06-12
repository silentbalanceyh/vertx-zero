package cn.vertxup.ui.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

public interface FieldStub {

    String OPTION_JSX = "optionJsx";
    String OPTION_CONFIG = "optionConfig";
    String OPTION_ITEM = "optionItem";
    String RULES = "rules";

    /*
     * Fetch all fields
     */
    Future<JsonArray> fetchUi(String formId);

    /*
     * update
     */
    Future<JsonArray> updateA(final String controlId, final JsonArray data);

    /*
     * delete by control id
     */
    Future<Boolean> deleteByControlId(String controlId);
}
