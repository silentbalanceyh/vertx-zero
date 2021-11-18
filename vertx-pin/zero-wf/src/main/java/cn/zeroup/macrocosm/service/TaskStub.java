package cn.zeroup.macrocosm.service;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface TaskStub {

    Future<JsonObject> fetchQueue(JsonObject condition);

    Future<JsonObject> fetchTodo(String key, String userId);

    Future<List<WTodo>> fetchHistory(WTodo todo);
}
