package io.vertx.tp.optic.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/*
 * XTodo Record fetching workflow
 */
public interface ExTodo {
    /*
     * Read data by options
     * 1）tid - XTodo id ( tid )
     * 2) params -> include ( modelId, modelKey, modelCategory, include sigma )
     */
    Future<JsonObject> fetchAsync(String id, JsonObject params);
}
