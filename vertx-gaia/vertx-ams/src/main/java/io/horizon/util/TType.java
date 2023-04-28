package io.horizon.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * @author lang : 2023/4/27
 */
final class TType {
    private TType() {
    }

    // （反射专用）Type类型处理部分
    static boolean isDate(final Class<?> clazz) {
        // < 1.8
        return Date.class.isAssignableFrom(clazz)
            // > 1.8
            || Temporal.class.isAssignableFrom(clazz);
    }

    static boolean isBoolean(final Class<?> clazz) {
        return Boolean.class.isAssignableFrom(clazz)
            || boolean.class.isAssignableFrom(clazz);
    }

    static boolean isVoid(final Class<?> clazz) {
        return void.class.isAssignableFrom(clazz)
            || Void.class.isAssignableFrom(clazz);
    }

    static boolean isInteger(final Class<?> clazz) {
        return Integer.class.isAssignableFrom(clazz)
            || int.class.isAssignableFrom(clazz)
            || Long.class.isAssignableFrom(clazz)
            || long.class.isAssignableFrom(clazz)
            || Short.class.isAssignableFrom(clazz)
            || short.class.isAssignableFrom(clazz)
            // 追加 BigInteger
            || BigInteger.class.isAssignableFrom(clazz);
    }

    static boolean isDecimal(final Class<?> clazz) {
        return Double.class.isAssignableFrom(clazz)
            || double.class.isAssignableFrom(clazz)
            || Float.class.isAssignableFrom(clazz)
            || float.class.isAssignableFrom(clazz)
            // 追加 BigDecimal
            || BigDecimal.class.isAssignableFrom(clazz);
    }

    static boolean isNumber(final Class<?> clazz) {
        return isDecimal(clazz)
            || isInteger(clazz)
            || Number.class.isAssignableFrom(clazz);
    }

    static boolean isJObject(final Class<?> clazz) {
        return JsonObject.class.isAssignableFrom(clazz)
            || LinkedHashMap.class.isAssignableFrom(clazz);
    }

    static boolean isClass(final Object clazz) {
        if (Objects.isNull(clazz)) {
            return false;
        }
        return Objects.nonNull(HInstance.clazz(clazz.toString(), null, null));
    }

    static boolean isJArray(final Class<?> clazz) {
        return JsonArray.class.isAssignableFrom(clazz);
    }

    static boolean isImplement(final Class<?> implCls, final Class<?> interfaceCls) {
        final Class<?>[] interfaces = implCls.getInterfaces();
        boolean match = Arrays.asList(interfaces).contains(interfaceCls);
        if (!match) {
            /* continue to check parent */
            if (Objects.nonNull(implCls.getSuperclass())) {
                match = isImplement(implCls.getSuperclass(), interfaceCls);
            }
        }
        return match;
    }
}
