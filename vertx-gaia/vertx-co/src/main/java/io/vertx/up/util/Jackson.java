package io.vertx.up.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.ZeroModule;
import io.reactivex.Observable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Lookup the json tree data
 */
@SuppressWarnings("all")
final class Jackson {
    private static final Annal LOGGER = Annal.get(Jackson.class);
    private static final JsonMapper MAPPER = new JsonMapper();

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
        JsonMapper.builder().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        JsonMapper.builder().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);
        // Jackson.MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        // Jackson.MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);

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

        Fn.verifyLenMin(Jackson.class, 0, keys);
        final JsonObject visited = Jackson.searchData(item, JsonObject.class, keys);
        if (Objects.isNull(visited)) {
            return new JsonObject();
        } else {
            return visited;
        }
    }

    static <T> T visitT(
        final JsonObject item,
        final String... keys
    ) {
        Fn.verifyLenMin(Jackson.class, 0, keys);
        return (T) Jackson.searchData(item, null, keys);
    }

    static JsonArray visitJArray(
        final JsonObject item,
        final String... keys
    ) {
        Fn.verifyLenMin(Jackson.class, 0, keys);
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
        Fn.verifyLenMin(Jackson.class, 0, keys);
        return Jackson.searchData(item, Integer.class, keys);
    }

    static String visitString(
        final JsonObject item,
        final String... keys
    ) {
        Fn.verifyLenMin(Jackson.class, 0, keys);
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
                    if (Objects.nonNull(clazz) && clazz == curVal.getClass()) {
                        // Strict Mode
                        result = (T) curVal;
                    } else {
                        // Cast Mode
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
                        result = Jackson.searchData(continueNode, clazz, continueKeys);
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
                // Fix issue of ["[]"] String literal
                if (!direct.isEmpty()) {
                    result.addAll(direct);
                }
/*                if (direct.isEmpty()) {
                    result.add(value.toString());
                } else {
                    result.addAll(direct);
                }*/
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

    static <T> String serialize(final T t) {
        return Fn.getNull(null, () -> Fn.getJvm(() -> Jackson.MAPPER.writeValueAsString(t), t), t);
    }

    static <T> T deserialize(final JsonObject value, final Class<T> type, final boolean isSmart) {
        return Fn.getNull(null,
            () -> Jackson.deserialize(value.encode(), type, isSmart), value);
    }

    static <T> T deserialize(final JsonArray value, final Class<T> type, final boolean isSmart) {
        return Fn.getNull(null,
            () -> Jackson.deserialize(value.encode(), type, isSmart), value);
    }

    static <T> List<T> deserialize(final JsonArray value, final TypeReference<List<T>> type) {
        return Fn.getNull(new ArrayList<>(),
            () -> Jackson.deserialize(value.encode(), type), value);
    }

    static <T> T deserialize(final String value, final Class<T> type, final boolean isSmart) {
        final String smart = isSmart ? deserializeSmart(value, type) : value;
        return Fn.getNull(null,
            () -> Fn.getJvm(() -> Jackson.MAPPER.readValue(smart, type)), value);
    }

    static <T> T deserialize(final String value, final TypeReference<T> type) {
        // Turn Off Smart Json when TypeReference<T>
        // final String smart = deserializeSmart(value, (Class<T>) type.getType());
        return Fn.getNull(null,
            () -> Fn.getJvm(() -> Jackson.MAPPER.readValue(value, type)), value);
    }

    static <T, R extends Iterable> R serializeJson(final T t, final boolean isSmart) {
        final String content = Jackson.serialize(t);
        return Fn.getJvm(null, () -> Fn.getSemi(content.trim().startsWith(Strings.LEFT_BRACE), null,
            /*
             * Switch to smart serialization on the object to avoid
             * issue when met {} or []
             * 递归调用
             */
            () -> isSmart ? serializeSmart(new JsonObject(content)) : ((R) new JsonObject(content)),
            () -> isSmart ? serializeSmart(new JsonArray(content)) : ((R) new JsonArray(content))
        ), content);
    }

    // ---------------------- Jackson Advanced for Smart Serilization / DeSerialization
    private static <T> String deserializeSmart(final String literal, final Class<T> type) {
        if (Types.isJObject(literal) || Types.isJArray(literal)) {
            if (Types.isJArray(literal)) {
                return deserializeSmart(new JsonArray(literal), type);
            } else {
                return deserializeSmart(new JsonObject(literal), type);
            }
        } else {
            return literal;
        }
    }

    private static <T> String deserializeSmart(final JsonObject item, final Class<T> type) {
        new HashSet<>(item.fieldNames()).forEach(field -> {
            try {
                final Field fieldT = type.getDeclaredField(field);
                final Class<?> fieldC = fieldT.getType();           // getType
                final Object value = item.getValue(field);
                /*
                 * Field type is `java.lang.String`
                 * Value is `JsonObject / JsonArray`
                 */
                if (value instanceof JsonObject && String.class == fieldC) {
                    item.put(field, ((JsonObject) value).encode());
                } else if (value instanceof JsonArray && String.class == fieldC) {
                    item.put(field, ((JsonArray) value).encode());
                }
            } catch (final NoSuchFieldException ex) {
                // Remove / Ignore the field
                item.remove(field);
            }
        });
        return item.encode();
    }

    private static <T> String deserializeSmart(final JsonArray item, final Class<T> type) {
        It.itJArray(item).forEach(json -> deserializeSmart(json, type));
        return item.encode();
    }

    private static <T, R extends Iterable> R serializeSmart(final JsonObject item) {
        new HashSet<>(item.fieldNames()).forEach(field -> {
            final Object value = item.getValue(field);
            if (value instanceof JsonObject) {
                item.put(field, serializeSmart((JsonObject) value));
            } else if (value instanceof JsonArray) {
                item.put(field, serializeSmart((JsonArray) value));
            } else if (value instanceof String) {
                // T -> JsonObject / JsonArray
                final String literal = (String) value;
                if (Types.isJArray(literal)) {
                    item.put(field, serializeSmart(new JsonArray(literal)));
                } else if (Types.isJObject(literal)) {
                    item.put(field, serializeSmart(new JsonObject(literal)));
                }
            }
        });
        return (R) item;
    }

    private static <T, R extends Iterable> R serializeSmart(final JsonArray item) {
        It.itJArray(item).forEach(json -> serializeSmart(json));
        return (R) item;
    }

    // ---------------------- Json Tool ----------------------------
    static JsonObject jsonMerge(final JsonObject target, final JsonObject source, final boolean isRef) {
        final JsonObject reference = isRef ? target : target.copy();
        reference.mergeIn(source, true);
        return reference;
    }

    static JsonObject jsonAppend(final JsonObject target, final JsonObject source, final boolean isRef) {
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

    static JsonArray sureJArray(final JsonArray array, final boolean copied) {
        if (Ut.isNil(array)) {
            return new JsonArray();
        } else {
            if (copied) {
                return array.copy();
            } else {
                return array;
            }
        }
    }

    static JsonArray sureJArray(final JsonArray array) {
        return sureJArray(array, false);
    }

    static JsonArray sureJArray(JsonObject input, final String field) {
        input = sureJObject(input);
        final Object value = input.getValue(field);
        if (Objects.isNull(value)) {
            return new JsonArray();
        }
        if (value instanceof JsonArray) {
            return (JsonArray) value;
        } else {
            LOGGER.warn("The value could not be converted to JsonArray = {0}", value);
            return new JsonArray();
        }
    }

    static JsonObject sureJObject(JsonObject input, final String field) {
        input = sureJObject(input);
        final Object value = input.getValue(field);
        if (Objects.isNull(value)) {
            return new JsonObject();
        }
        if (value instanceof JsonObject) {
            return (JsonObject) value;
        } else {
            LOGGER.warn("The value could not be converted to JsonObject = {0}", value);
            return new JsonObject();
        }
    }

    static JsonObject sureJObject(final JsonObject object, final boolean copied) {
        if (Ut.isNil(object)) {
            return new JsonObject();
        } else {
            if (copied) {
                return object.copy();
            } else {
                return object;
            }
        }
    }

    static JsonObject sureJObject(final JsonObject object) {
        return sureJObject(object, false);
    }

    static String aiJArray(final String literal) {
        if (literal.contains(Strings.QUOTE_DOUBLE)) {
            return literal;
        } else {
            final StringBuilder buffer = new StringBuilder();
            final String[] split = literal.split(Strings.COMMA);
            final List<String> elements = new ArrayList<>();
            Arrays.stream(split).forEach(each -> {
                if (Objects.nonNull(each)) {
                    if (each.trim().startsWith(Strings.LEFT_SQUARE)) {
                        elements.add(Strings.QUOTE_DOUBLE +
                            each.trim().substring(1)
                            + Strings.QUOTE_DOUBLE);
                    } else if (each.trim().endsWith(Strings.RIGHT_SQUARE)) {
                        elements.add(Strings.QUOTE_DOUBLE +
                            each.trim().substring(0, each.trim().length() - 1)
                            + Strings.QUOTE_DOUBLE);
                    } else {
                        elements.add(Strings.QUOTE_DOUBLE +
                            each.trim()
                            + Strings.QUOTE_DOUBLE);
                    }
                }
            });
            buffer.append(Strings.LEFT_SQUARE);
            buffer.append(Ut.fromJoin(elements));
            buffer.append(Strings.RIGHT_SQUARE);
            return buffer.toString();
        }
    }

    static ChangeFlag flag(final JsonObject recordN, final JsonObject recordO) {
        if (Types.isEmpty(recordO)) {
            if (Types.isEmpty(recordN)) {
                return ChangeFlag.NONE;
            } else {
                /* Old = null, New = not null, ADD */
                return ChangeFlag.ADD;
            }
        } else {
            if (Types.isEmpty(recordN)) {
                /* Old = not null, New = null, DELETE */
                return ChangeFlag.DELETE;
            } else {
                return ChangeFlag.UPDATE;
            }
        }
    }

    static ChangeFlag flag(final JsonObject data) {
        final JsonObject copy = Ut.valueJObject(data);
        final String flag = copy.getString(KName.__.FLAG, ChangeFlag.NONE.name());
        return Ut.toEnum(ChangeFlag.class, flag);
    }


    static JsonObject data(final JsonObject input, final boolean previous) {
        final JsonObject copy = Ut.valueJObject(input).copy();
        if (previous) {
            /*
             * Previous Old Data
             */
            return Ut.valueJObject(input, KName.__.DATA);
        } else {
            copy.remove(KName.Flow.WORKFLOW);           // Remove workflow
            copy.remove(KName.__.USER);                 // Remove user
            copy.remove(KName.__.DATA);
            copy.remove(KName.__.INPUT);
            copy.remove(KName.__.FLAG);
            return copy;
        }
    }
}
