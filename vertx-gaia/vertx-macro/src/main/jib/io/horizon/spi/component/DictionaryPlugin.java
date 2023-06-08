package io.horizon.spi.component;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.exchange.DSource;

public interface DictionaryPlugin {

    default DictionaryPlugin configuration(final JsonObject configuration) {
        return this;
    }

    Future<JsonArray> fetchAsync(DSource source, MultiMap paramMap);

    default JsonArray fetch(final DSource source, final MultiMap paramMap) {
        return new JsonArray();
    }
}
