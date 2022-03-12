package cn.vertxup.integration.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface DirStub {

    Future<JsonObject> create(JsonObject directoryJ);

    Future<JsonObject> update(String key, JsonObject directoryJ);

    Future<Boolean> remove(String key, boolean actual);
}
