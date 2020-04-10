package io.vertx.up.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

class It {
    static java.util.stream.Stream<JsonObject> itJArray(final JsonArray array) {
        return array.stream().filter(item -> item instanceof JsonObject).map(item -> (JsonObject) item);
    }

    static java.util.stream.Stream<JsonObject> itJArray(final JsonArray array, final Predicate<JsonObject> predicate) {
        return itJArray(array).filter(predicate);
    }

    static java.util.stream.Stream<String> itJString(final JsonArray array) {
        return array.stream().filter(item -> item instanceof String).map(item -> (String) item);
    }

    static java.util.stream.Stream<String> itJString(final JsonArray array, final Predicate<String> predicate) {
        return itJString(array, predicate);
    }

    @SuppressWarnings("all")
    static <T> T itJson(final T data, final Function<JsonObject, T> executor) {
        if (Objects.isNull(data)) {
            return null;
        } else {
            if (data instanceof JsonObject) {
                final JsonObject reference = (JsonObject) data;
                return executor.apply(reference);
            } else if (data instanceof JsonArray) {
                final JsonArray normalized = new JsonArray();
                final JsonArray reference = (JsonArray) data;
                itJArray(reference)
                        .map(each -> itJson(each, (json) -> executor.apply(json)))
                        .forEach(normalized::add);
                return (T) normalized;
            } else return data;
        }
    }
}
