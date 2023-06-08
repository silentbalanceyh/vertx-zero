package io.vertx.up.uca.serialization;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;
import java.util.*;

public class SaberTypes {

    static final Set<Class<?>> NATIVE =
        new HashSet<Class<?>>() {
            {
                add(int.class);
                add(Integer.class);
                add(short.class);
                add(Short.class);
                add(double.class);
                add(Double.class);
                add(long.class);
                add(Long.class);
                add(boolean.class);
                add(Boolean.class);
                add(float.class);
                add(Float.class);
                add(JsonObject.class);
                add(JsonArray.class);
                add(String.class);
                add(byte[].class);
                add(Byte[].class);
                add(byte.class);
                add(Byte.class);
            }
        };

    static final Set<Class<?>> SUPPORTED =
        new HashSet<Class<?>>() {
            {
                addAll(NATIVE);
                add(Date.class);
                add(StringBuffer.class);
                add(StringBuilder.class);
                add(Calendar.class);
                add(Buffer.class);
                add(BigDecimal.class);
                add(Enum.class);
                add(Collection.class);
                add(Set.class);
                add(List.class);
            }
        };

    public static <T> boolean isNative(final T input) {
        if (null == input) {
            return false;
        }
        final Class<?> type = input.getClass();
        return NATIVE.contains(type);
    }

    public static <T> boolean isSupport(final Class<?> inputCls) {
        if (null == inputCls) {
            return false;
        }
        return SUPPORTED.contains(inputCls);
    }
}
