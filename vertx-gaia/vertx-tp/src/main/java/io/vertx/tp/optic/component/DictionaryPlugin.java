package io.vertx.tp.optic.component;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.exchange.DiSource;

public interface DictionaryPlugin {

    default DictionaryPlugin configuration(final JsonObject configuration) {
        return this;
    }

    Future<JsonArray> fetchAsync(DiSource source, MultiMap paramMap);

    default JsonArray fetch(DiSource source, MultiMap paramMap) {
        return new JsonArray();
    }
}
