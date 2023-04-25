package io.horizon.spi.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/*
 * Channel for application
 */
public interface ExApp {
    /*
     * Fetch App
     * 1) Add `options` field to appJson
     * 2) Provide custom calculation method for `options`
     */
    Future<JsonObject> fetchOpts(JsonObject appJson);
}
