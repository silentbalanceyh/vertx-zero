package io.vertx.up.util;

import io.horizon.uca.convert.V;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.exchange.BMapping;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/*
 * Convert the value to the type value that
 * JsonObject could accept here.
 * It means returned type must be accepted by `JsonObject`
 * The types that JsonObject support:
 * - null
 * - java.lang.Boolean, boolean.class
 * - java.lang.byte[],  byte[].class
 * - java.lang.Double,  double.class
 * - java.lang.Float,   float.class
 * - java.lang.Enum,    ????
 * - java.time.Instant,
 * - java.lang.Integer, integer.class
 * - java.lang.Long,    long.class
 * - `JsonObject`
 * - `JsonArray`
 */
class Value {

    private static Object aiValue(final Object input, final Class<?> type,
                                  final Function<Class<?>, Class<?>> targetFn,
                                  final BiFunction<Class<?>, Class<?>, Object> executor) {
        if (Objects.isNull(input)) {
            /*
             * input = null;
             */
            return null;
        } else {
            if ("null".equalsIgnoreCase(input.toString()) ||
                "undefined".equalsIgnoreCase(input.toString())) {
                /*
                 * null, NULL -> null
                 * undefined, -> null
                 */
                return null;
            } else {
                final Class<?> sourceType = input.getClass();
                Class<?> targetType = type;
                if (Objects.isNull(targetType)) {
                    /*
                     * Source first
                     */
                    targetType = targetFn.apply(sourceType);
                } else {
                    /*
                     * Target then
                     */
                    targetType = targetFn.apply(targetType);
                }
                return executor.apply(sourceType, targetType);
            }
        }
    }

    /*
     * Json to Type
     */
    @SuppressWarnings("all")
    static Object aiJValue(final Object input, final Class<?> type) {
        return aiValue(input, type, Value::aiJType, (sourceType, targetType) -> aiExecute(sourceType, targetType, input,
            (source) -> {
                /*
                 * If String, check whether it's date
                 */
                if (String.class != type && Ut.isDate(source.toString())) {
                    /*
                     * String to
                     */
                    return V.vInstant().to(source, sourceType);
                } else {
                    return input.toString();
                }
            }));
    }

    /*
     * Java to Type
     */
    @SuppressWarnings("all")
    static Object aiValue(final Object input, final Class<?> type) {
        return aiValue(input, type, Value::aiType, (sourceType, targetType) -> aiExecute(sourceType, targetType, input,
            (source) -> {
                /*
                 * If String, check whether it's date
                 */
                if (String.class != type && Ut.isDate(source.toString())) {
                    /*
                     * String to
                     */
                    return V.vDate().to(source, sourceType);
                } else {
                    return input.toString();
                }
            }));
    }

    private static Class<?> aiJType(final Class<?> type) {
        if (LocalDateTime.class == type || LocalDate.class == type || LocalTime.class == type || Date.class == type) {
            /*
             * - java.util.Date
             * - java.time.LocalTime
             * - java.time.LocalDateType
             * - java.time.LocalTime
             */
            return Instant.class;
        } else {
            return aiUnit(type);
        }
    }

    private static Class<?> aiType(final Class<?> type) {
        if (LocalDateTime.class == type || LocalDate.class == type || LocalTime.class == type || Instant.class == type) {
            /*
             * - java.util.Instant
             * - java.time.LocalTime
             * - java.time.LocalDateType
             * - java.time.LocalTime
             */
            return type;
        } else {
            return aiUnit(type);
        }
    }

    private static Class<?> aiUnit(final Class<?> type) {
        if (Long.class == type || long.class == type) {
            /*
             * - java.lang.Long
             * - java.lang.long
             */
            return Long.class;
        } else if (Short.class == type || short.class == type) {
            /*
             * - java.lang.Short
             * - java.lang.short
             */
            return Short.class;
        } else if (Integer.class == type || int.class == type) {
            /*
             * - java.lang.Integer
             * - java.lang.int
             */
            return Integer.class;
        } else if (Boolean.class == type || boolean.class == type) {
            /*
             * - java.lang.Boolean
             * - java.lang.boolean
             */
            return Boolean.class;
        } else if (Double.class == type || double.class == type || BigDecimal.class == type) {
            /*
             * - java.lang.Double
             * - java.lang.double
             * - java.util.BigDecimal
             */
            return Double.class;
        } else if (Float.class == type || float.class == type) {
            /*
             * - java.lang.Float
             * - java.lang.float
             */
            return Float.class;
        } else if (JsonArray.class == type) {
            /*
             * - JsonArray
             */
            return JsonArray.class;
        } else if (JsonObject.class == type) {
            /*
             * - JsonObject
             */
            return JsonObject.class;
        } else {
            /*
             * - Other
             */
            return String.class;
        }
    }

    @SuppressWarnings("unchecked")
    private static Object aiExecute(final Class<?> sourceType, final Class<?> targetType, final Object input,
                                    final Function<Object, Object> addonFn) {

        if (Instant.class == targetType) {
            /*
             * Strict:     -        java.time.Instant
             * Compatible: -        java.lang.String
             */
            return V.vInstant().to(input, sourceType);
        } else if (Integer.class == targetType) {
            /*
             * Strict:     -        java.lang.Integer
             */
            return V.vInteger().to(input, sourceType);
        } else if (Long.class == targetType) {
            /*
             * Strict:     -       java.lang.Long
             */
            return V.vLong().to(input, sourceType);
        } else if (Short.class == targetType) {
            /*
             * Strict:     -       java.lang.Short
             */
            return V.vShort().to(input, sourceType);
        } else if (Float.class == targetType) {
            /*
             * Strict:     -       java.lang.Float
             */
            return V.vFloat().to(input, sourceType);
        } else if (Double.class == targetType) {
            /*
             * Strict:     -       java.lang.Double
             */
            return V.vDouble().to(input, sourceType);
        } else if (Boolean.class == targetType) {
            /*
             * Strict:     -       java.lang.Boolean
             */
            return V.vBoolean().to(input, sourceType);
        } else if (Date.class == targetType) {
            /*
             * Strict:     -       java.util.Date
             */
            return V.vDate().to(input, sourceType);
        } else if (JsonArray.class == targetType) {
            /*
             * Strict:     -       JsonArray
             */
            return Ut.toJArray(input);
        } else if (JsonObject.class == targetType) {
            /*
             * Strict:     -       JsonArray
             */
            return Ut.toJObject(input);
        } else if (String.class == targetType) {
            return addonFn.apply(input);
        }
        if (String.class == sourceType) {
            if (LocalDateTime.class == targetType) {
                final Date date = Ut.parseFull(input.toString());
                return Ut.toDateTime(date);
            } else if (LocalDate.class == targetType) {
                final Date date = Ut.parseFull(input.toString());
                return Ut.toDate(date);
            } else if (LocalTime.class == targetType) {
                final Date date = Ut.parseFull(input.toString());
                return Ut.toTime(date);
            }
        }
        return null;
    }

    static JsonObject aiIn(final JsonObject in, final BMapping mapping, final boolean keepNil) {
        if (Objects.isNull(mapping)) {
            /*
             * No mapping
             */
            return in.copy();
        } else {
            /*
             * Mapping configured
             */
            final JsonObject normalized = new JsonObject();
            in.fieldNames().forEach(field -> {
                /*
                 * field is (To) field,
                 * convert to standard model attribute
                 */
                final String fromField = mapping.from(field);
                if (Ut.isNil(fromField)) {
                    if (keepNil) {
                        normalized.put(field, in.getValue(field));
                    }
                } else {
                    normalized.put(fromField, in.getValue(field));
                }
            });
            return normalized;
        }
    }

    static JsonObject aiOut(final JsonObject out, final BMapping mapping, final boolean keepNil) {
        if (Objects.isNull(mapping)) {
            /*
             * No mapping
             */
            return out.copy();
        } else {
            final JsonObject normalized = new JsonObject();
            out.fieldNames().forEach(field -> {
                /*
                 * field is (From) field
                 */
                final String fromField = mapping.to(field);
                if (Ut.isNil(fromField)) {
                    if (keepNil) {
                        normalized.put(field, out.getValue(field));
                    }
                } else {
                    normalized.put(fromField, out.getValue(field));
                }
            });
            return normalized;
        }
    }
}
