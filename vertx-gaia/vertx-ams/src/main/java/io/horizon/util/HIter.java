package io.horizon.util;

import io.horizon.fn.Actuator;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

class HIter {
    static <T> Stream<T> itSet(final Set<T> set) {
        final Set<T> source = Objects.isNull(set) ? new HashSet<>() : set;
        return itCollection(source);
    }

    static <T> Stream<T> itList(final List<T> list) {
        final List<T> source = Objects.isNull(list) ? new ArrayList<>() : list;
        return itCollection(source);
    }

    private static <T> Stream<T> itCollection(final Collection<T> source) {
        // 并行
        return source.stream().filter(Objects::nonNull);
    }

    static Stream<JsonObject> itJArray(final JsonArray array) {
        final JsonArray source = HaS.valueJArray(array);
        // 并行
        return source.stream().filter(item -> item instanceof JsonObject).map(item -> (JsonObject) item);
    }

    static Stream<JsonObject> itJArray(final JsonArray array, final Predicate<JsonObject> predicate) {
        return itJArray(array).filter(predicate);
    }

    static Stream<String> itJString(final JsonArray array) {
        final JsonArray source = HaS.valueJArray(array);
        // 并行
        return source.stream().filter(item -> item instanceof String).map(item -> (String) item);
    }

    static Stream<String> itJString(final JsonArray array, final Predicate<String> predicate) {
        return itJString(array).filter(predicate);
    }

    @SuppressWarnings("unchecked")
    static <T> T itJson(final T data, final Function<JsonObject, T> executor) {
        if (Objects.isNull(data)) {
            return null;
        } else {
            if (data instanceof final JsonObject reference) {
                return executor.apply(reference);
            } else if (data instanceof final JsonArray reference) {
                final JsonArray normalized = new JsonArray();
                itJArray(reference)
                    .map(each -> itJson(each, (json) -> executor.apply(json)))
                    .forEach(normalized::add);
                return (T) normalized;
            } else {
                return data;
            }
        }
    }

    static void itRepeat(final Integer times, final Actuator actuator) {
        int start = 0;
        while (start < times) {
            actuator.execute();
            start++;
        }
    }
}
