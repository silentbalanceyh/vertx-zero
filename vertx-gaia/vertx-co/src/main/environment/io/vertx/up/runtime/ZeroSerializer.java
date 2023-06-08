package io.vertx.up.runtime;

import io.horizon.uca.cache.Cc;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.secure.Vis;
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
import java.util.function.Supplier;

/**
 * ZeroSerializer the request by different type.
 * 1. String -> T
 * 2. T -> JsonObject ( Envelop request )
 * 3. T -> String ( Generate resonse )
 * 4. Checking the request type to see where support serialization
 */
public class ZeroSerializer {

    private static final Cc<String, Saber> CC_SABER = Cc.openThread();
    private static final ConcurrentMap<Class<?>, Supplier<Saber>> SABERS =
        new ConcurrentHashMap<>() {
            {
                this.put(int.class, supplier(IntegerSaber.class));
                this.put(Integer.class, supplier(IntegerSaber.class));
                this.put(short.class, supplier(ShortSaber.class));
                this.put(Short.class, supplier(ShortSaber.class));
                this.put(long.class, supplier(LongSaber.class));
                this.put(Long.class, supplier(LongSaber.class));

                this.put(double.class, supplier(DoubleSaber.class));
                this.put(Double.class, supplier(DoubleSaber.class));

                this.put(LocalDate.class, supplier(Java8DataTimeSaber.class));
                this.put(LocalDateTime.class, supplier(Java8DataTimeSaber.class));
                this.put(LocalTime.class, supplier(Java8DataTimeSaber.class));

                this.put(float.class, supplier(FloatSaber.class));
                this.put(Float.class, supplier(FloatSaber.class));
                this.put(BigDecimal.class, supplier(BigDecimalSaber.class));

                this.put(Enum.class, supplier(EnumSaber.class));

                this.put(boolean.class, supplier(BooleanSaber.class));
                this.put(Boolean.class, supplier(BooleanSaber.class));

                this.put(Date.class, supplier(DateSaber.class));
                this.put(Calendar.class, supplier(DateSaber.class));

                this.put(JsonObject.class, supplier(JsonObjectSaber.class));
                this.put(JsonArray.class, supplier(JsonArraySaber.class));

                this.put(String.class, supplier(StringSaber.class));
                this.put(StringBuffer.class, supplier(StringBufferSaber.class));
                this.put(StringBuilder.class, supplier(StringBufferSaber.class));

                this.put(Buffer.class, supplier(BufferSaber.class));
                this.put(Set.class, supplier(CollectionSaber.class));
                this.put(List.class, supplier(CollectionSaber.class));
                this.put(Collection.class, supplier(CollectionSaber.class));

                this.put(byte[].class, supplier(ByteArraySaber.class));
                this.put(Byte[].class, supplier(ByteArraySaber.class));

                this.put(File.class, supplier(FileSaber.class));
                this.put(Vis.class, supplier(VisSaber.class));
            }
        };

    private static Supplier<Saber> supplier(final Class<?> clazz) {
        return () -> CC_SABER.pick(() -> Ut.instance(clazz), clazz.getName());
    }

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
                final Supplier<Saber> supplier = SABERS.get(Enum.class);
                saber = supplier.get();
            } else if (Collection.class.isAssignableFrom(paramType)) {
                final Supplier<Saber> supplier = SABERS.get(Collection.class);
                saber = supplier.get();
            } else {
                final Supplier<Saber> supplier = SABERS.get(paramType);
                saber = supplier.get();
            }
            if (null == saber) {
                saber = supplier(CommonSaber.class).get();
            }
            reference = saber.from(paramType, literal);
        }
        return reference;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValueT(final Class<?> paramType,
                                  final String literal) {
        return (T) getValue(paramType, literal);
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
                    final Supplier<Saber> supplier = SABERS.get(Enum.class);
                    saber = supplier.get();
                } else if (Calendar.class.isAssignableFrom(cls)) {
                    final Supplier<Saber> supplier = SABERS.get(Date.class);
                    saber = supplier.get();
                } else if (Collection.class.isAssignableFrom(cls)) {
                    final Supplier<Saber> supplier = SABERS.get(Collection.class);
                    saber = supplier.get();
                } else if (Buffer.class.isAssignableFrom(cls)) {
                    final Supplier<Saber> supplier = SABERS.get(Buffer.class);
                    saber = supplier.get();
                } else if (cls.isArray()) {
                    final Class<?> type = cls.getComponentType();
                    if (byte.class == type || Byte.class == type) {
                        final Supplier<Saber> supplier = SABERS.get(byte[].class);
                        saber = supplier.get();
                    } else {
                        final Supplier<Saber> supplier = SABERS.get(Collection.class);
                        saber = supplier.get(); //  SABERS.get(Collection.class);
                    }
                } else {
                    final Supplier<Saber> supplier = SABERS.get(cls);
                    saber = supplier.get();
                }
                if (null == saber) {
                    saber = supplier(CommonSaber.class).get();
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
