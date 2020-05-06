package io.vertx.tp.optic.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.unity.Ux;

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

    /*
     * Valve XTodo data for creation
     * Default implementation is `No valve`
     */
    default Future<JsonArray> valveAsync(final JsonArray input, final ChangeFlag type) {
        return Ux.future(input);
    }
}
