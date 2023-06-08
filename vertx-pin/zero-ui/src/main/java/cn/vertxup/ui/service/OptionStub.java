package cn.vertxup.ui.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface OptionStub {
    String FIELD_SEARCH_NOTICE = "advancedNotice";
    String FIELD_SEARCH_VIEW = "advancedView";
    String FIELD_SEARCH_COND = "cond";

    String FIELD_TABLE_OP_CONFIG = "opConfig";

    String FIELD_FRAGMENT_MODEL = "modal";
    String FIELD_FRAGMENT_NOTICE = "notice";
    String FIELD_FRAGMENT_CONFIG = "config";
    String FIELD_FRAGMENT_BUTTON_GROUP = "buttonGroup";

    String FIELD_QUERY_PROJECTION = "projection";
    String FIELD_QUERY_CRITERIA = "criteria";

    String FIELD_OP_CONFIG = "config";
    String FIELD_OP_PLUGIN = "plugin";

    /* V_QUERY */
    Future<JsonObject> fetchQuery(String id);

    /* V_SEARCH */
    Future<JsonObject> fetchSearch(String id);

    /* V_FRAGMENT */
    Future<JsonObject> fetchFragment(String id);

    /* V_TABLE */
    Future<JsonObject> fetchTable(String id);

    /*
     * update
     */
    Future<JsonArray> updateA(String controlId, JsonArray data);

    /*
     * delete by control id
     */
    Future<Boolean> deleteByControlId(String controlId);
}
