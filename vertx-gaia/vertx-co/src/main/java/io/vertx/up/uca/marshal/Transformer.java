package io.vertx.up.uca.marshal;

import io.vertx.core.json.JsonObject;

public interface Transformer<T> {

    T transform(JsonObject input);
}
