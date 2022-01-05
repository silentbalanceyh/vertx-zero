package cn.vertxup.fm.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface EndStub {

    // Fetch Book with bill and items
    Future<JsonObject> fetchSettlement(String key);
}
