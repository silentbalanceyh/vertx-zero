package io.horizon.util;

import io.horizon.annotations.ChatGPT;
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
        final JsonArray source = HUt.valueJArray(array);
        // 并行
        return source.stream().filter(item -> item instanceof JsonObject).map(item -> (JsonObject) item);
    }

    static Stream<JsonObject> itJArray(final JsonArray array, final Predicate<JsonObject> predicate) {
        return itJArray(array).filter(predicate);
    }

    static Stream<String> itJString(final JsonArray array) {
        final JsonArray source = HUt.valueJArray(array);
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

    // itJObject 的强化版
    @SuppressWarnings("all")
    @ChatGPT
    static <T> Stream<Map.Entry<String, T>> itJObject(final JsonObject input, final Class<T> clazz) {
        return input.stream().filter(entry -> isMatch(entry.getValue(), clazz)).map(entry -> {
            // convert entry.getValue() to T
            final T t = (T) entry.getValue();
            final String key = entry.getKey();
            // call the BiConsumer provided in the forEach method with key k and value t
            // Note that the lambda expression should have the same parameter types as the BiConsumer interface
            // In this case, the first parameter is a String and the second parameter is of type T
            // So the forEach method should be called like this: forEach((String k, T t) -> {})
            return new AbstractMap.SimpleEntry(key, t);
        });
    }

    @SuppressWarnings("all")
    static <T> Stream<T> itJArray(final JsonArray input, final Class<T> clazz) {
        return input.stream().filter(item -> isMatch(item, clazz))
            .map(item -> (T) item);
    }

    private static <T> boolean isMatch(final Object value, final Class<T> clazz) {
        // 过滤掉 value 为 null 的元素
        if (Objects.isNull(clazz)) {
            return true;
        }
        // clazz 不为 null 才执行此处检查，过滤掉 value 为 null 的元素
        if (Objects.isNull(value)) {
            return false;   // 直接过滤
        }
        return clazz.isInstance(value) || clazz.isAssignableFrom(value.getClass());
    }
}
