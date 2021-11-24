package cn.zeroup.macrocosm.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface TaskStub {

    Future<JsonObject> fetchQueue(JsonObject condition);

    Future<JsonObject> fetchTodo(String key, String userId);

    Future<JsonObject> fetchFinished(String key);
}
