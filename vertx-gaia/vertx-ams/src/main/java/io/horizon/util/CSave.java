package io.horizon.util;

import io.horizon.eon.VValue;
import io.horizon.fn.HFn;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * @author lang : 2023/4/30
 */
class CSave {

    static JsonArray save(final JsonArray array, final JsonArray input, final String field) {
        HIter.itJArray(input).forEach(json -> save(array, json, field));
        return array;
    }

    static JsonArray save(final JsonArray array, final JsonObject json, final String field) {
        return HFn.runOr(new JsonArray(), () -> {
            final AtomicBoolean isFound = new AtomicBoolean(Boolean.FALSE);
            HUt.itJArray(array).forEach(each -> {
                final boolean isSame = isSameBy(each, json, field);
                if (isSame) {
                    each.mergeIn(json, true);
                    isFound.set(Boolean.TRUE);
                }
            });
            if (!isFound.get()) {
                /*
                 * Not found, add
                 */
                array.add(json.copy());
            }
            return array;
        }, array, json, field);
    }

    static <T, V> List<T> save(final List<T> list, final T entity, final Function<T, V> keyFn) {
        if (Objects.isNull(entity)) {
            return list;
        }
        final V keyAdd = keyFn.apply(entity);
        if (Objects.isNull(keyAdd)) {
            return list;
        }
        int foundIdx = VValue.RANGE;
        for (int idx = VValue.IDX; idx < list.size(); idx++) {
            final T original = list.get(idx);
            if (Objects.isNull(original)) {
                continue;
            }
            final V keyOld = keyFn.apply(original);
            if (keyAdd.equals(keyOld)) {
                foundIdx = idx;
                break;
            }
        }
        if (VValue.RANGE == foundIdx) {
            list.add(entity);
        } else {
            list.set(foundIdx, entity);
        }
        return list;
    }

    private static boolean isSameBy(final Object left, final Object right, final String field) {
        // 同时为 null
        if (Objects.isNull(left) && Objects.isNull(right)) {
            return true;
        }
        // 其中一个为 null
        if (Objects.isNull(left) || Objects.isNull(right)) {
            return false;
        }
        // 类型不匹配
        if (left.getClass() != right.getClass()) {
            return false;
        }
        // 最终比较
        if (left instanceof JsonObject && right instanceof JsonObject) {
            final Object leftValue = ((JsonObject) left).getValue(field);
            final Object rightValue = ((JsonObject) right).getValue(field);
            return isSameBy(leftValue, rightValue, field);
        } else {
            return left.equals(right);
        }
    }
}
