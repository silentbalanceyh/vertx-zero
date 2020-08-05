package cn.vertxup.ui.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public interface OpStub {

    /*
     * Fetch ops by control, dynamic part for usage
     */
    Future<JsonArray> fetchDynamic(String control);

    /*
     * Fetch ops by identifier, fixed part for usage
     */
    Future<JsonArray> fetchFixed(String identifier);
}
