package io.horizon.spi.plugin;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface BeforePlugin extends DataPlugin<BeforePlugin> {

    Future<JsonObject> beforeAsync(JsonObject record, JsonObject options);

    Future<JsonArray> beforeAsync(JsonArray records, JsonObject options);
}
