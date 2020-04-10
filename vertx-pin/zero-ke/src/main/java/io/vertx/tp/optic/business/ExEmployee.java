package io.vertx.tp.optic.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/*
 * User Information get from system
 */
public interface ExEmployee {
    /*
     * Read data by `id`
     */
    Future<JsonObject> fetchAsync(String id);

    /*
     * Update data by `id`
     */
    Future<JsonObject> updateAsync(String id, JsonObject params);
}
