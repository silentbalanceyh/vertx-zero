package io.vertx.tp.optic.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Set;

/*
 * User Information get from system
 */
public interface ExEmployee {
    /*
     * Read data by `id`
     */
    Future<JsonObject> fetchAsync(String id);

    Future<JsonArray> fetchAsync(Set<String> ids);

    /*
     * Update data by `id`
     */
    Future<JsonObject> updateAsync(String id, JsonObject params);
}
