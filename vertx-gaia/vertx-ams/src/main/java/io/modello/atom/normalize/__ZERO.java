package io.modello.atom.normalize;

import io.horizon.util.HUt;
import io.modello.eon.em.Marker;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

/**
 * @author lang : 2023-05-09
 */
class MarkUtil {
    private static final Boolean[] FIELD_VALUE = new Boolean[]{
        true,               // 0 = active
        true,               // 1 = track
        false,              // 2 = lock
        true,               // 3 = confirm
        false,              // 4 = array
        true,               // 5 = syncIn
        true,               // 6 = syncOut
        false,              // 7 = refer
    };
    /*
     * field = boolean
     */
    private static final ConcurrentMap<String, Boolean> FIELD_DEFAULT = new ConcurrentHashMap<>();

    static {
        final int length = Marker.NAMES.length;
        for (int idx = 0; idx < length; idx++) {
            FIELD_DEFAULT.put(Marker.NAMES[idx], FIELD_VALUE[idx]);
        }
    }

    static ConcurrentMap<String, Boolean> parse(final JsonObject inputJ) {
        final JsonObject input = Objects.isNull(inputJ) ? new JsonObject() : inputJ;
        final ConcurrentMap<String, Boolean> result = new ConcurrentHashMap<>();
        Arrays.stream(Marker.NAMES).forEach(field -> {
            final Boolean parsed;
            if (input.containsKey(field)) {
                parsed = input.getBoolean(field, FIELD_DEFAULT.get(field));
            } else {
                parsed = FIELD_DEFAULT.get(field);
            }
            result.put(field, parsed);
        });
        return result;
    }

    static boolean value(final String field, final ConcurrentMap<String, Boolean> parsed) {
        final boolean valueDefault = FIELD_DEFAULT.get(field);
        return parsed.getOrDefault(field, valueDefault);
    }

    static ConcurrentMap<String, Boolean> parse(final List<String> valueList) {
        final ConcurrentMap<String, Boolean> result = new ConcurrentHashMap<>();
        if (Objects.isNull(valueList) || Marker.NAMES.length != valueList.size()) {
            /*
             * 约束限定，两种情况返回默认值
             * 1. 如果传入的 valueList 是 null
             * 2. 如果传入的 valueList 是 empty
             */
            result.putAll(FIELD_DEFAULT);
        } else {
            // 对齐 Marker.NAMES
            final int actual = valueList.size();
            for (int idx = 0; idx < Marker.NAMES.length; idx++) {
                // 如果 idx 索引合法
                final String field = Marker.NAMES[idx];
                if (idx < (actual - 1)) {
                    final String value = valueList.get(idx);
                    result.put(field, parseValue(field, value));
                } else {
                    result.put(field, FIELD_DEFAULT.get(field));
                }
            }
        }
        return result;
    }

    static String toString(final String name,
                           final ConcurrentMap<String, Boolean> valueMap) {
        final StringBuilder builder = new StringBuilder();
        builder.append(name).append("{");
        Arrays.stream(Marker.NAMES).forEach(field -> {
            if (valueMap.containsKey(field)) {
                builder.append(field).append("=").append(valueMap.get(field)).append(",");
            } else {
                builder.append(field).append("=null,");
            }
        });
        builder.append("}");
        return builder.toString();
    }

    private static Boolean parseValue(final String field, final String literal) {
        if (Objects.isNull(literal) || "NULL".equalsIgnoreCase(literal)) {
            // 返回默认值
            return FIELD_DEFAULT.get(field);
        } else if ("1".equals(literal)) {
            // Because parseBoolean / valueOf will be false, here must support 1 = true
            return true;
        } else {
            // Other string will start parsing workflow ( standard )
            return Boolean.parseBoolean(literal);
        }
    }
}


final class RRuler {
    private RRuler() {
    }

    public static JsonArray required(final JsonArray source, final RRule rule) {
        /* required fields */
        return rulerAnd(source, rule.getRequired(), value -> HUt.isNotNil(value.toString()));
    }

    public static JsonArray duplicated(final JsonArray source, final RRule rule) {
        /* unique field */
        final Set<JsonObject> added = new HashSet<>();
        return ruler(source, rule.getUnique(), json -> {
            final JsonObject uniqueJson = HUt.elementSubset(json, rule.getUnique());
            if (added.contains(uniqueJson)) {
                return false;
            } else {
                added.add(uniqueJson);
                return true;
            }
        });
    }

    private static JsonArray rulerAnd(final JsonArray source, final Set<String> fieldSet, final Predicate<Object> fnFilter) {
        return ruler(source, fieldSet, json -> fieldSet.stream().allMatch(field -> {
            final Object value = json.getValue(field);
            if (Objects.nonNull(value)) {
                return fnFilter.test(value);
            } else {
                return false;
            }
        }));
    }

    private static JsonArray rulerOr(final JsonArray source, final Set<String> fieldSet, final Predicate<Object> fnFilter) {
        return ruler(source, fieldSet, json -> fieldSet.stream().anyMatch(field -> {
            final Object value = json.getValue(field);
            if (Objects.nonNull(value)) {
                return fnFilter.test(value);
            } else {
                return false;
            }
        }));
    }

    private static JsonArray ruler(final JsonArray source, final Set<String> fieldSet, final Predicate<JsonObject> fnFilter) {
        if (fieldSet.isEmpty()) {
            return source;
        } else {
            /* Code Logical */
            final JsonArray processed = new JsonArray();
            HUt.itJArray(source).filter(fnFilter).forEach(processed::add);
            return processed;
        }
    }
}