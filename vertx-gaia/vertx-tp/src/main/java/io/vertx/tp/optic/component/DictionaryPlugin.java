package io.vertx.tp.optic.component;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.DictSource;

public interface DictionaryPlugin {

    default DictionaryPlugin configuration(final JsonObject configuration) {
        return this;
    }

    Future<JsonArray> fetchAsync(DictSource source, MultiMap paramMap);
}
