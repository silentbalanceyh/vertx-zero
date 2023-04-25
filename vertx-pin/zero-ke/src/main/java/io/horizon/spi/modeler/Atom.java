package io.horizon.spi.modeler;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Set;

/**
 * AoDao fetching for dynamic data part here
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Atom {

    /*
     * Single ->
     * 1. Create
     * 2. Update
     * 3. Fetch
     */
    Future<JsonObject> createAsync(String identifier, JsonObject data);

    Future<JsonObject> updateAsync(String identifier, String key, JsonObject data);

    Future<JsonObject> fetchAsync(String identifier, String key);

    /*
     * Batch ->
     * 1. Create
     * 2. Update
     * 3. Fetch
     */
    Future<JsonArray> createAsync(String identifier, JsonArray data);

    Future<JsonArray> updateAsync(String identifier, Set<String> keys, JsonArray data);

    Future<JsonArray> fetchAsync(String identifier, Set<String> keys);
}
