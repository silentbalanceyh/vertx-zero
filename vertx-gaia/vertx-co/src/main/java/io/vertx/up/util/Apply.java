package io.vertx.up.util;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

class Apply {

    @SuppressWarnings("all")
    static <T, V> Consumer<JsonObject> applyField(final String field, final Function<V, T> function) {
        return (json) -> {
            json = Jackson.sureJObject(json);
            if (json.containsKey(field)) {
                final Object value = json.getValue(field);
                if (Objects.nonNull(value)) {
                    function.apply((V) value);
                }
            }
        };
    }

    static <T> Function<T, Future<JsonObject>> applyField(final JsonObject input, final String field) {
        return data -> {
            if (Ut.isNil(field)) {
                if (data instanceof JsonObject) {
                    final JsonObject dataR = (JsonObject) data;
                    input.mergeIn(dataR);
                }
            } else {
                input.put(field, data);
            }
            return Future.succeededFuture(input);
        };
    }

    static <I, T> Function<I, Future<T>> applyNil(final Supplier<T> supplier, final Supplier<Future<T>> executor) {
        return input -> {
            if (Objects.isNull(input)) {
                return Future.succeededFuture(supplier.get());
            } else {
                return executor.get();
            }
        };
    }

    static <I, T> Function<I, Future<T>> applyNil(final Supplier<T> supplier, final Function<I, Future<T>> executor) {
        return input -> {
            if (Objects.isNull(input)) {
                return Future.succeededFuture(supplier.get());
            } else {
                return executor.apply(input);
            }
        };
    }

    static <T> Function<T, Future<T>> applyNil(final Function<T, Future<T>> executor) {
        return input -> {
            if (Objects.isNull(input)) {
                return Future.succeededFuture(input);
            } else {
                return executor.apply(input);
            }
        };
    }

    static <T> Function<T[], Future<T[]>> applyEmpty(final Function<T[], Future<T[]>> executor) {
        return input -> {
            if (Objects.isNull(input) || 0 == input.length) {
                return Future.succeededFuture(input);
            } else {
                return executor.apply(input);
            }
        };
    }

    static Function<JsonArray, Future<JsonArray>> applyJEmpty(final Function<JsonArray, Future<JsonArray>> executor) {
        return input -> {
            if (Objects.isNull(input) || input.isEmpty()) {
                return Future.succeededFuture(input);
            } else {
                return executor.apply(input);
            }
        };
    }

    static JsonObject applyJValue(final JsonObject record, final String field, final Object value) {
        if (Objects.isNull(record)) {
            return null;
        } else {
            final Object originalValue = record.getValue(field);
            /*
             * Modification for
             * 1) Not null
             * 2) Include `key` not existing
             */
            if (Objects.isNull(originalValue)) {
                record.put(field, value);
            }
            return record;
        }
    }

    static JsonObject applyJCopy(final JsonObject record, final String from, final String to) {
        if (Objects.isNull(record)) {
            return null;
        } else {
            final Object value = record.getValue(from);
            if (Objects.nonNull(value)) {
                record.put(to, value);
            }
            return record;
        }
    }

    static Function<JsonArray, Future<JsonArray>> ifStrings(final String... fields) {
        return jarray -> {
            It.itJArray(jarray).forEach(json ->
                    Arrays.stream(fields).forEach(field -> ifString(json, field)));
            return Future.succeededFuture(jarray);
        };
    }

    static Function<JsonObject, Future<JsonObject>> ifString(final String... fields) {
        return json -> {
            Arrays.stream(fields).forEach(field -> ifString(json, field));
            return Future.succeededFuture(json);
        };
    }

    static Function<JsonObject, Future<JsonObject>> ifJObject(final String... fields) {
        return json -> {
            Arrays.stream(fields).forEach(field -> ifJson(json, field));
            return Future.succeededFuture(json);
        };
    }

    static Function<JsonArray, Future<JsonArray>> ifJArray(final String... fields) {
        return jarray -> {
            It.itJArray(jarray).forEach(json ->
                    Arrays.stream(fields).forEach(field -> ifJson(json, field)));
            return Future.succeededFuture(jarray);
        };
    }

    static void ifString(final JsonObject json, final String field) {
        if (!Types.isEmpty(json)) {
            final Object value = json.getValue(field);
            if (Objects.nonNull(value)) {
                if (value instanceof JsonObject) {
                    final String literal = ((JsonObject) value).encode();
                    json.put(field, literal);
                } else if (value instanceof JsonArray) {
                    final String literal = ((JsonArray) value).encode();
                    json.put(field, literal);
                }
            }
        }
    }

    static void ifString(final JsonObject json, final String... fields) {
        Arrays.stream(fields).forEach(field -> ifString(json, field));
    }

    static void ifJson(final JsonObject json, final String field) {
        if (!Types.isEmpty(json)) {
            final Object value = json.getValue(field);
            if (Objects.nonNull(value)) {
                final String literal = value.toString();
                if (Types.isJObject(literal)) {
                    final JsonObject replaced = To.toJObject(literal);
                    json.put(field, replaced);
                } else if (Types.isJArray(literal)) {
                    final JsonArray replaced = To.toJArray(literal);
                    json.put(field, replaced);
                }
            }
        }
    }

    static void ifJson(final JsonObject json, final String... fields) {
        Arrays.stream(fields).forEach(field -> ifJson(json, field));
    }
}
