package io.vertx.up.runtime;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.uca.serialization.*;
import io.vertx.up.util.Ut;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ZeroSerializer the request by different type.
 * 1. String -> T
 * 2. T -> JsonObject ( Envelop request )
 * 3. T -> String ( Generate resonse )
 * 4. Checking the request type to see where support serialization
 */
public class ZeroSerializer {

    private static final ConcurrentMap<Class<?>, Saber> SABERS =
        new ConcurrentHashMap<Class<?>, Saber>() {
            {
                this.put(int.class, Ut.singleton(IntegerSaber.class));
                this.put(Integer.class, Ut.singleton(IntegerSaber.class));
                this.put(short.class, Ut.singleton(ShortSaber.class));
                this.put(Short.class, Ut.singleton(ShortSaber.class));
                this.put(long.class, Ut.singleton(LongSaber.class));
                this.put(Long.class, Ut.singleton(LongSaber.class));

                this.put(double.class, Ut.singleton(DoubleSaber.class));
                this.put(Double.class, Ut.singleton(DoubleSaber.class));

                this.put(LocalDate.class, Ut.singleton(Java8DataTimeSaber.class));
                this.put(LocalDateTime.class, Ut.singleton(Java8DataTimeSaber.class));
                this.put(LocalTime.class, Ut.singleton(Java8DataTimeSaber.class));

                this.put(float.class, Ut.singleton(FloatSaber.class));
                this.put(Float.class, Ut.singleton(FloatSaber.class));
                this.put(BigDecimal.class, Ut.singleton(BigDecimalSaber.class));

                this.put(Enum.class, Ut.singleton(EnumSaber.class));

                this.put(boolean.class, Ut.singleton(BooleanSaber.class));
                this.put(Boolean.class, Ut.singleton(BooleanSaber.class));

                this.put(Date.class, Ut.singleton(DateSaber.class));
                this.put(Calendar.class, Ut.singleton(DateSaber.class));

                this.put(JsonObject.class, Ut.singleton(JsonObjectSaber.class));
                this.put(JsonArray.class, Ut.singleton(JsonArraySaber.class));

                this.put(String.class, Ut.singleton(StringSaber.class));
                this.put(StringBuffer.class, Ut.singleton(StringBufferSaber.class));
                this.put(StringBuilder.class, Ut.singleton(StringBufferSaber.class));

                this.put(Buffer.class, Ut.singleton(BufferSaber.class));
                this.put(Set.class, Ut.singleton(CollectionSaber.class));
                this.put(List.class, Ut.singleton(CollectionSaber.class));
                this.put(Collection.class, Ut.singleton(CollectionSaber.class));

                this.put(byte[].class, Ut.singleton(ByteArraySaber.class));
                this.put(Byte[].class, Ut.singleton(ByteArraySaber.class));

                this.put(File.class, Ut.singleton(FileSaber.class));
            }
        };

    /**
     * String -> T
     *
     * @param paramType argument types
     * @param literal   literal values
     *
     * @return deserialized object.
     */
    public static Object getValue(final Class<?> paramType,
                                  final String literal) {
        Object reference = null;
        if (null != literal) {
            Saber saber;
            if (paramType.isEnum()) {
                saber = SABERS.get(Enum.class);
            } else if (Collection.class.isAssignableFrom(paramType)) {
                saber = SABERS.get(Collection.class);
            } else {
                saber = SABERS.get(paramType);
            }
            if (null == saber) {
                saber = Ut.singleton(CommonSaber.class);
            }
            reference = saber.from(paramType, literal);
        }
        return reference;
    }

    public static <T> boolean isDirect(final T input) {
        boolean result = false;
        if (null != input) {
            final Class<?> cls = input.getClass();
            if (JsonObject.class != cls && JsonArray.class != cls) {
                result = SABERS.containsKey(cls);
            }
        }
        return result;
    }

    /**
     * T -> JsonObject
     *
     * @param input Checked object
     * @param <T>   Generic Types
     *
     * @return returned values.
     */
    public static <T> Object toSupport(final T input) {
        try {
            Object reference = null;
            if (null != input) {
                Saber saber;
                final Class<?> cls = input.getClass();
                if (cls.isEnum()) {
                    saber = SABERS.get(Enum.class);
                } else if (Calendar.class.isAssignableFrom(cls)) {
                    saber = SABERS.get(Date.class);
                } else if (Collection.class.isAssignableFrom(cls)) {
                    saber = SABERS.get(Collection.class);
                } else if (cls.isArray()) {
                    final Class<?> type = cls.getComponentType();
                    if (byte.class == type || Byte.class == type) {
                        saber = SABERS.get(byte[].class);
                    } else {
                        saber = SABERS.get(Collection.class);
                    }
                } else {
                    saber = SABERS.get(cls);
                }
                if (null == saber) {
                    saber = Ut.singleton(CommonSaber.class);
                }
                reference = saber.from(input);
            }
            return reference;
        } catch (final Throwable ex) {
            /*
             * Serialization debug for data
             */
            ex.printStackTrace();
            return null;
        }
    }
}
