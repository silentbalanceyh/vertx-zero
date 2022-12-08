package io.vertx.up.uca.serialization;

import io.vertx.core.json.JsonArray;

import java.util.function.Function;

/**
 * JsonArray
 */
@SuppressWarnings("unchecked")
public class JsonArraySaber extends JsonSaber {
    @Override
    protected boolean isValid(final Class<?> paramType) {
        return JsonArray.class == paramType;
    }

    @Override
    protected Function<String, JsonArray> getFun() {
        return JsonArray::new;
    }
}
