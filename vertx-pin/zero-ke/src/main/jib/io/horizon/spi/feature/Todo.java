package io.horizon.spi.feature;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

/*
 * WTodo Record fetching workflow
 */
public interface Todo {
    /*
     * Read data by options
     * 1）tid - WTodo id ( tid )
     * 2) params -> include ( modelId, modelKey, modelCategory, include sigma )
     */
    Future<JsonObject> fetchAsync(String id, JsonObject params);

    /*
     * Valve WTodo data for creation
     * Default implementation is `No valve`
     */
    default Future<JsonArray> valveAsync(final JsonArray input, final JsonObject params) {
        return Ux.future(input);
    }
}
