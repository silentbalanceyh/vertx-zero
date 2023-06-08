package io.vertx.up.util;

import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.uca.log.Annal;
import io.horizon.util.HUt;
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

    private Jackson() {
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
                    if (HUt.isJObject(curVal)) {
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
        HUt.itJArray(array).forEach(json -> {
            final T fieldV = (T) json.getValue(fieldFrom, null);
            final JsonArray data;
            if (Objects.nonNull(fieldV)) {
                data = grouped.getOrDefault(fieldV, new JsonArray());
            } else {
                data = new JsonArray();
            }
            if (HUt.isNil(fieldTo)) {
                json.put(fieldOn, data);
            } else {
                final JsonArray replaced = new JsonArray();
                HUt.itJArray(data).forEach(each -> replaced.add(each.getValue(fieldTo)));
                json.put(fieldOn, replaced);
            }
        });
        return array;
    }

    static <T> T deserialize(final JsonObject value, final Class<T> type, final boolean isSmart) {
        return Fn.runOr(null,
            () -> Jackson.deserialize(value.encode(), type, isSmart), value);
    }

    static <T> T deserialize(final JsonArray value, final Class<T> type, final boolean isSmart) {
        return Fn.runOr(null,
            () -> Jackson.deserialize(value.encode(), type, isSmart), value);
    }

    static <T> T deserialize(final String value, final Class<T> type, final boolean isSmart) {
        final String smart = isSmart ? deserializeSmart(value, type) : value;
        return Fn.runOr(null,
            () -> Fn.failOr(() -> HUt.deserialize(smart, type)), value);
    }

    static <T, R extends Iterable> R serializeJson(final T t, final boolean isSmart) {
        final String content = HUt.serialize(t);
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
        if (HUt.isJObject(literal) || HUt.isJArray(literal)) {
            if (HUt.isJArray(literal)) {
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
        HUt.itJArray(item).forEach(json -> deserializeSmart(json, type));
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
                if (HUt.isJArray(literal)) {
                    item.put(field, serializeSmart(new JsonArray(literal)));
                } else if (HUt.isJObject(literal)) {
                    item.put(field, serializeSmart(new JsonObject(literal)));
                }
            }
        });
        return (R) item;
    }

    private static <T, R extends Iterable> R serializeSmart(final JsonArray item) {
        HUt.itJArray(item).forEach(json -> serializeSmart(json));
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
        if (HUt.isNil(recordO)) {
            if (HUt.isNil(recordN)) {
                return ChangeFlag.NONE;
            } else {
                /* Old = null, New = not null, ADD */
                return ChangeFlag.ADD;
            }
        } else {
            if (HUt.isNil(recordN)) {
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
        if (HUt.isNil(literal)) {
            return null;
        }
        final String trimInput = literal.trim();
        if (trimInput.startsWith(VString.LEFT_BRACE)) {
            return (T) HUt.toJObject(literal);
        } else if (trimInput.startsWith(VString.LEFT_SQUARE)) {
            return (T) HUt.toJArray(literal);
        }
        return null;
    }
}
