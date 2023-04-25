package io.horizon.spi.plugin;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface AfterPlugin extends DataPlugin<AfterPlugin> {

    Future<JsonObject> afterAsync(JsonObject record, JsonObject options);

    Future<JsonArray> afterAsync(JsonArray records, JsonObject options);
}
