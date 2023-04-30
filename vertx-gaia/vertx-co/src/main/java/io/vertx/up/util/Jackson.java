package io.vertx.up.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.OriginalNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.ZeroModule;
import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.horizon.eon.em.ChangeFlag;
import io.horizon.uca.log.Annal;
import io.horizon.util.HaS;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * Lookup the json tree data
 */
@SuppressWarnings("all")
final class Jackson {
    private static final Annal LOGGER = Annal.get(Jackson.class);
    private static final JsonMapper MAPPER = JsonMapper.builder()
        /*
         * Previous code
         * JsonMapper.builder().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
         * JsonMapper.builder().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);
         * Jackson.MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
         * Jackson.MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);
         *
         * Case Sensitive
         * Below new code logical
         */
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
        .build();

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

        final ZeroModule module = new ZeroModule();
        Jackson.MAPPER.registerModule(module);
        Jackson.MAPPER.setPropertyNamingStrategy(OriginalNamingStrategy.JOOQ_NAME);
    }

    private Jackson() {
    }

    static JsonMapper getMapper() {
        return MAPPER.copy();
    }

    static JsonObject visitJObject(
        final JsonObject item,
        final String... keys
    ) {

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
        return (T) Jackson.searchData(item, null, keys);
    }

    static JsonArray visitJArray(
        final JsonObject item,
        final String... keys
    ) {
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
        return Jackson.searchData(item, Integer.class, keys);
    }

    static String visitString(
        final JsonObject item,
        final String... keys
    ) {
        return Jackson.searchData(item, String.class, keys);
    }

    private static <T> T searchData(final JsonObject data,
                                    final Class<T> clazz,
                                    final String... pathes) {
        if (null == data || VValue.ZERO == pathes.length) {
            return null;
        }
        /* 1. Get current node  **/
        final JsonObject current = data.copy();
        /* 2. Extract current input key **/
        final String path = pathes[VValue.IDX];
        /* 3. Continue searching if key existing, otherwise terminal. **/
        return Fn.runOr(current.containsKey(path) && null != current.getValue(path),
            null,
            () -> {
                final Object curVal = current.getValue(path);
                T result = null;
                if (VValue.ONE == pathes.length) {
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
                    if (HaS.isJObject(curVal)) {
                        final JsonObject continueNode = current.getJsonObject(path);
                        /* 4.Extract new key **/
                        final String[] continueKeys =
                            Arrays.copyOfRange(pathes,
                                VValue.ONE,
                                pathes.length);
                        result = Jackson.searchData(continueNode, clazz, continueKeys);
                    }
                }
                return result;
            },
            () -> null);
    }

    @SuppressWarnings("unchecked")
    static <T> JsonArray zip(final JsonArray array, final String fieldFrom,
                             final String fieldOn,
                             final ConcurrentMap<T, JsonArray> grouped, final String fieldTo) {
        HaS.itJArray(array).forEach(json -> {
            final T fieldV = (T) json.getValue(fieldFrom, null);
            final JsonArray data;
            if (Objects.nonNull(fieldV)) {
                data = grouped.getOrDefault(fieldV, new JsonArray());
            } else {
                data = new JsonArray();
            }
            if (HaS.isNil(fieldTo)) {
                json.put(fieldOn, data);
            } else {
                final JsonArray replaced = new JsonArray();
                HaS.itJArray(data).forEach(each -> replaced.add(each.getValue(fieldTo)));
                json.put(fieldOn, replaced);
            }
        });
        return array;
    }

    static <T> String serialize(final T t) {
        return Fn.runOr(null, () -> Fn.failOr(() -> Jackson.MAPPER.writeValueAsString(t), t), t);
    }

    static <T> T deserialize(final JsonObject value, final Class<T> type, final boolean isSmart) {
        return Fn.runOr(null,
            () -> Jackson.deserialize(value.encode(), type, isSmart), value);
    }

    static <T> T deserialize(final JsonArray value, final Class<T> type, final boolean isSmart) {
        return Fn.runOr(null,
            () -> Jackson.deserialize(value.encode(), type, isSmart), value);
    }

    static <T> List<T> deserialize(final JsonArray value, final TypeReference<List<T>> type) {
        return Fn.runOr(new ArrayList<>(),
            () -> Jackson.deserialize(value.encode(), type), value);
    }

    static <T> T deserialize(final String value, final Class<T> type, final boolean isSmart) {
        final String smart = isSmart ? deserializeSmart(value, type) : value;
        return Fn.runOr(null,
            () -> Fn.failOr(() -> Jackson.MAPPER.readValue(smart, type)), value);
    }

    static <T> T deserialize(final String value, final TypeReference<T> type) {
        // Turn Off Smart Json when TypeReference<T>
        // final String smart = deserializeSmart(value, (Class<T>) type.getType());
        return Fn.runOr(null,
            () -> Fn.failOr(() -> Jackson.MAPPER.readValue(value, type)), value);
    }

    static <T, R extends Iterable> R serializeJson(final T t, final boolean isSmart) {
        final String content = Jackson.serialize(t);
        return Fn.failOr(null, () -> Fn.runOr(content.trim().startsWith(VString.LEFT_BRACE), null,
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
        if (HaS.isJObject(literal) || HaS.isJArray(literal)) {
            if (HaS.isJArray(literal)) {
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
        HaS.itJArray(item).forEach(json -> deserializeSmart(json, type));
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
                if (HaS.isJArray(literal)) {
                    item.put(field, serializeSmart(new JsonArray(literal)));
                } else if (HaS.isJObject(literal)) {
                    item.put(field, serializeSmart(new JsonObject(literal)));
                }
            }
        });
        return (R) item;
    }

    private static <T, R extends Iterable> R serializeSmart(final JsonArray item) {
        HaS.itJArray(item).forEach(json -> serializeSmart(json));
        return (R) item;
    }

    // ---------------------- Json Tool ----------------------------

    static String aiJArray(final String literal) {
        if (literal.contains(VString.QUOTE_DOUBLE)) {
            return literal;
        } else {
            final StringBuilder buffer = new StringBuilder();
            final String[] split = literal.split(VString.COMMA);
            final List<String> elements = new ArrayList<>();
            Arrays.stream(split).forEach(each -> {
                if (Objects.nonNull(each)) {
                    if (each.trim().startsWith(VString.LEFT_SQUARE)) {
                        elements.add(VString.QUOTE_DOUBLE +
                            each.trim().substring(1)
                            + VString.QUOTE_DOUBLE);
                    } else if (each.trim().endsWith(VString.RIGHT_SQUARE)) {
                        elements.add(VString.QUOTE_DOUBLE +
                            each.trim().substring(0, each.trim().length() - 1)
                            + VString.QUOTE_DOUBLE);
                    } else {
                        elements.add(VString.QUOTE_DOUBLE +
                            each.trim()
                            + VString.QUOTE_DOUBLE);
                    }
                }
            });
            buffer.append(VString.LEFT_SQUARE);
            buffer.append(Ut.fromJoin(elements));
            buffer.append(VString.RIGHT_SQUARE);
            return buffer.toString();
        }
    }

    static ChangeFlag flag(final JsonObject recordN, final JsonObject recordO) {
        if (HaS.isNil(recordO)) {
            if (HaS.isNil(recordN)) {
                return ChangeFlag.NONE;
            } else {
                /* Old = null, New = not null, ADD */
                return ChangeFlag.ADD;
            }
        } else {
            if (HaS.isNil(recordN)) {
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
        return Ut.toEnum(flag, ChangeFlag.class);
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

    static String encodeJ(final Object value) {
        if (value instanceof JsonObject) {
            return ((JsonObject) value).encode();
        }
        if (value instanceof JsonArray) {
            return ((JsonArray) value).encode();
        }
        return Objects.isNull(value) ? VString.EMPTY : value.toString();
    }

    @SuppressWarnings("unchecked")
    static <T> T decodeJ(final String literal) {
        if (HaS.isNil(literal)) {
            return null;
        }
        final String trimInput = literal.trim();
        if (trimInput.startsWith(VString.LEFT_BRACE)) {
            return (T) HaS.toJObject(literal);
        } else if (trimInput.startsWith(VString.LEFT_SQUARE)) {
            return (T) HaS.toJArray(literal);
        }
        return null;
    }
}
