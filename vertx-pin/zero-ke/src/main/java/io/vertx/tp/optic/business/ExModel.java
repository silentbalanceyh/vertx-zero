package io.vertx.tp.optic.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentMap;

/*
 * Model information for some connected points usage.
 */
public interface ExModel {

    Future<ConcurrentMap<String, JsonObject>> keyAsync(String sigma, JsonArray users);

    Future<JsonArray> fetchAsync(String sigma);

    Future<JsonArray> fetchAttrs(String identifier, String sigma);
}
