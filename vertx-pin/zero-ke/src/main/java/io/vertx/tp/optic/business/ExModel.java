package io.vertx.tp.optic.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentMap;

/*
 * Model information for some connected points usage.
 */
public interface ExModel {
    /*
     * Read all model information to fill
     * 1) Tabular -> identifier
     * 2) Category -> identifier
     */
    Future<JsonArray> fetchAsync(String sigma);

    Future<ConcurrentMap<String, JsonObject>> keyAsync(String sigma, JsonArray users);
}
