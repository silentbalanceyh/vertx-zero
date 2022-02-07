package cn.zeroup.macrocosm.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WRecord;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface TaskStub {
    /*
     * W_TODO JOIN W_TICKET
     */
    Future<JsonObject> fetchQueue(JsonObject condition);

    Future<JsonObject> fetchHistory(JsonObject condition);

    /*
     * WTicket + WTodo
     */
    Future<WRecord> readRecord(String todoKey);

    /*
     * Fetch detail information
     */
    Future<JsonObject> readPending(String key, String userId);

    Future<JsonObject> readFinished(String key);
}
