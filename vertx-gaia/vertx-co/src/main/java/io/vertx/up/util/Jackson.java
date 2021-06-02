package io.vertx.up.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.ZeroModule;
import io.reactivex.Observable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Lookup the json tree data
 */
@SuppressWarnings({"all"})
final class Jackson {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        // Ignore null value
        Jackson.MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Non-standard JSON but we allow C style comments in our JSON
        Jackson.MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        Jackson.MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        Jackson.MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Big Decimal
        Jackson.MAPPER.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        // Case Sensitive
        Jackson.MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        Jackson.MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);

        final ZeroModule module = new ZeroModule();
        Jackson.MAPPER.registerModule(module);
        Jackson.MAPPER.setPropertyNamingStrategy(OriginalNamingStrategy.JOOQ_NAME);
    }

    private Jackson() {
    }

    static ObjectMapper getMapper() {
        return MAPPER.copy();
    }

    static JsonObject visitJObject(
            final JsonObject item,
            final String... keys
    ) {

        Fn.inLenMin(Jackson.class, 0, keys);
        final JsonObject visited = Jackson.searchData(item, JsonObject.class, keys);
        if (Objects.isNull(visited)) {
            return new JsonObject();
        } else {
            return visited;
        }
    }

    static JsonArray visitJArray(
            final JsonObject item,
            final String... keys
    ) {
        Fn.inLenMin(Jackson.class, 0, keys);
        final JsonArray visited = Jackson.searchData(item, JsonArray.class, keys);
        if (Objects.isNull(visited)) {
            return new JsonArray();
        } else {
            return visited;
        }
    }

    static Integer visitInt(
            final JsonObject item,
            final String... keys
    ) {
        Fn.inLenMin(Jackson.class, 0, keys);
        return Jackson.searchData(item, Integer.class, keys);
    }

    static String visitString(
            final JsonObject item,
            final String... keys
    ) {
        Fn.inLenMin(Jackson.class, 0, keys);
        return Jackson.searchData(item, String.class, keys);
    }

    private static <T> T searchData(final JsonObject data,
                                    final Class<T> clazz,
                                    final String... pathes) {
        if (null == data || Values.ZERO == pathes.length) {
            return null;
        }
        /* 1. Get current node  **/
        final JsonObject current = data.copy();
        /* 2. Extract current input key **/
        final String path = pathes[Values.IDX];
        /* 3. Continue searching if key existing, otherwise terminal. **/
        return Fn.getSemi(current.containsKey(path) && null != current.getValue(path),
                null,
                () -> {
                    final Object curVal = current.getValue(path);
                    T result = null;
                    if (Values.ONE == pathes.length) {
                        /* 3.1. Get the end node. **/
                        if (clazz == curVal.getClass()) {
                            result = (T) curVal;
                        }
                    } else {
                        /* 3.2. Address the middle search **/
                        if (Types.isJObject(curVal)) {
                            final JsonObject continueNode = current.getJsonObject(path);
                            /* 4.Extract new key **/
                            final String[] continueKeys =
                                    Arrays.copyOfRange(pathes,
                                            Values.ONE,
                                            pathes.length);
                            result = Jackson.searchData(continueNode,
                                    clazz,
                                    continueKeys);
                        }
                    }
                    return result;
                },
                () -> null);
    }

    static JsonArray mergeZip(final JsonArray source, final JsonArray target,
                              final String sourceKey, final String targetKey) {
        final JsonArray result = new JsonArray();
        Fn.safeJvm(() -> Observable.fromIterable(source)
                .filter(Objects::nonNull)
                .map(item -> (JsonObject) item)
                .map(item -> item.mergeIn(Jackson.findByKey(target, targetKey, item.getValue(sourceKey))))
                .subscribe(result::add).dispose(), null);
        return result;
    }

    private static JsonObject findByKey(final JsonArray source,
                                        final String key,
                                        final Object value) {
        return Fn.getJvm(() -> Observable.fromIterable(source)
                .filter(Objects::nonNull)
                .map(item -> (JsonObject) item)
                .filter(item -> null != item.getValue(key))
                .filter(item -> value == item.getValue(key) || item.getValue(key).equals(value))
                .first(new JsonObject()).blockingGet(), source, key);
    }

    static JsonArray toJArray(final Object value) {
        final JsonArray result = new JsonArray();
        Fn.safeNull(() -> {
            if (Types.isJArray(value)) {
                result.addAll((JsonArray) value);
            } else {
                final JsonArray direct = To.toJArray(value.toString());
                if (direct.isEmpty()) {
                    result.add(value.toString());
                } else {
                    result.addAll(direct);
                }
            }
        }, value);
        return result;
    }

    static JsonObject toJObject(final Object value) {
        final JsonObject result = new JsonObject();
        Fn.safeNull(() -> {
            if (Types.isJObject(value)) {
                result.mergeIn((JsonObject) value, true);
            } else {
                result.mergeIn(To.toJObject(value.toString()), true);
            }
        }, value);
        return result;
    }

    static <T, R extends Iterable> R serializeJson(final T t) {
        final String content = Jackson.serialize(t);
        return Fn.getJvm(null,
                () -> Fn.getSemi(content.trim().startsWith(Strings.LEFT_BRACES), null,
                        () -> (R) new JsonObject(content),
                        () -> (R) new JsonArray(content)), content);
    }

    static <T> String serialize(final T t) {
        return Fn.getNull(null, () -> Fn.getJvm(() -> Jackson.MAPPER.writeValueAsString(t), t), t);
    }

    static <T> T deserialize(final JsonObject value, final Class<T> type) {
        return Fn.getNull(null,
                () -> Jackson.deserialize(value.encode(), type), value);
    }

    static <T> T deserialize(final JsonArray value, final Class<T> type) {
        return Fn.getNull(null,
                () -> Jackson.deserialize(value.encode(), type), value);
    }

    static <T> List<T> deserialize(final JsonArray value, final TypeReference<List<T>> type) {
        return Fn.getNull(new ArrayList<>(),
                () -> Jackson.deserialize(value.encode(), type), value);
    }

    static <T> T deserialize(final String value, final Class<T> type) {
        return Fn.getNull(null,
                () -> Fn.getJvm(() -> Jackson.MAPPER.readValue(value, type)), value);
    }

    static <T> T deserialize(final String value, final TypeReference<T> type) {
        return Fn.getNull(null,
                () -> Fn.getJvm(() -> Jackson.MAPPER.readValue(value, type)), value);
    }

    // ---------------------- Json Tool ----------------------------
    static JsonObject jsonMerge(final JsonObject target, final JsonObject source, boolean isRef) {
        final JsonObject reference = isRef ? target : target.copy();
        reference.mergeIn(source, true);
        return reference;
    }

    static JsonObject jsonAppend(final JsonObject target, final JsonObject source, boolean isRef) {
        final JsonObject reference = isRef ? target : target.copy();
        source.fieldNames().stream()
                .filter(field -> !reference.containsKey(field))
                .forEach(field -> reference.put(field, source.getValue(field)));
        return reference;
    }

    static void jsonCopy(final JsonObject target, final JsonObject source, final String... fields) {
        Arrays.stream(fields).forEach(field -> Fn.safeNull(() -> {
            final Object value = source.getValue(field);
            if (Objects.nonNull(value)) {
                target.put(field, value);
            }
        }, target, source, field));
    }
}
