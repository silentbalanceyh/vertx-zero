package io.horizon.spi.environment;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentMap;

/*
 * Model information for some connected points usage.
 */
public interface Modeling {

    Future<ConcurrentMap<String, JsonObject>> keyAsync(String sigma, JsonArray users);

    Future<JsonArray> fetchAsync(String sigma);

    Future<JsonArray> fetchAttrs(String identifier, String sigma);
}
