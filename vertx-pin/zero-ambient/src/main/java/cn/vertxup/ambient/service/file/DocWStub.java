package cn.vertxup.ambient.service.file;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface DocWStub {

    Future<JsonArray> upload(JsonArray documentA);

    Future<JsonObject> rename(JsonObject documentJ);

    Future<JsonArray> trashIn(JsonArray documentA);

    Future<JsonArray> trashOut(JsonArray documentA);

    Future<JsonArray> trashKo(JsonArray documentA);
}
