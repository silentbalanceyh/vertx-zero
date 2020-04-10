package io.vertx.up.util;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Strings;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

/*
 * Specific checking
 */
class Is {

    static boolean isSame(final Object left, final Object right, final String field) {
        if (Objects.isNull(left) && Objects.isNull(right)) {
            return true;
        } else {
            if (Objects.isNull(left) || Objects.isNull(right)) {
                return false;
            } else {
                if (left.getClass() != right.getClass()) {
                    return false;
                } else {
                    if (left instanceof JsonObject && right instanceof JsonObject) {
                        final Object leftValue = ((JsonObject) left).getValue(field);
                        final Object rightValue = ((JsonObject) right).getValue(field);
                        return isSame(leftValue, rightValue, field);
                    } else {
                        return left.equals(right);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    static <T> boolean isEqual(final JsonObject record, final String field, final T expected) {
        if (Types.isEmpty(record)) {
            /*
             * If record is null or empty, return `false`
             */
            return false;
        } else {
            /*
             * Object reference
             */
            final Object value = record.getValue(field);
            if (Objects.isNull(value)) {
                /*
                 * Also `null`
                 */
                return false;
            } else {
                /*
                 * Compared
                 */
                return ((T) value).equals(expected);
            }
        }
    }

    /*
     * Whether record contains all the data in cond.
     * JsonObject subset here for checking
     */
    static boolean isSubset(final JsonObject cond, final JsonObject record) {
        final Set<String> fields = cond.fieldNames();
        final long counter = fields.stream()
                /* record contains all cond */
                .filter(record::containsKey)
                .filter(field -> Compare.equal(record.getValue(field), cond.getValue(field)))
                .count();
        return fields.size() == counter;
    }

    static boolean isChanged(final JsonObject oldRecord, final JsonObject newRecord,
                             final Set<String> ignores, final ConcurrentMap<String, Class<?>> types,
                             final BiFunction<String, Class<?>, BiPredicate<Object, Object>> fnPredicate) {
        /*
         * copy each compared json object and remove
         * all fields that will not be compared here.
         */
        final JsonObject oldCopy = oldRecord.copy();
        final JsonObject newCopy = newRecord.copy();
        if (Objects.nonNull(ignores) && !ignores.isEmpty()) {
            ignores.forEach(oldCopy::remove);
            ignores.forEach(newCopy::remove);
        }
        /*
         * Date Time conversation
         */
        final ConcurrentMap<String, Class<?>> dateFields = new ConcurrentHashMap<>();
        types.forEach((field, type) -> {
            if (Types.isDate(type)) {
                dateFields.put(field, type);
            }
        });
        final Set<String> dateFieldSet = dateFields.keySet();

        final Function<String, Boolean> isSame = (field) -> {
            /*
             * Extract value from each record
             */
            final Object oldValue = oldCopy.getValue(field);
            final Object newValue = newCopy.getValue(field);
            final TemporalUnit unit = getUnit(dateFields.get(field));
            final boolean basic = isSame(oldValue, newValue, dateFieldSet.contains(field), unit);
            if (basic) {
                return Boolean.TRUE;
            } else {
                if (Objects.isNull(fnPredicate)) {
                    return Boolean.FALSE;
                } else {
                    final Class<?> type = dateFields.get(field);
                    final BiPredicate<Object, Object> predicate = fnPredicate.apply(field, type);
                    return predicate.test(oldValue, newValue);
                }
            }
        };
        /*
         * Get the final result of calculation.
         * 1) From old calculation
         */
        final boolean unchanged = oldCopy.fieldNames().stream().allMatch(isSame::apply);
        /*
         * 2) From new calculation
         */
        final Set<String> newLefts = new HashSet<>(newCopy.fieldNames());
        newLefts.removeAll(oldCopy.fieldNames());
        final boolean additional = newLefts.stream().allMatch(isSame::apply);
        return !(unchanged && additional);
    }

    static TemporalUnit getUnit(final Class<?> clazz) {
        final TemporalUnit unit;
        if (LocalDateTime.class == clazz || LocalTime.class == clazz) {
            /*
             * 按分钟
             */
            unit = ChronoUnit.MINUTES;
        } else {
            /*
             * 某天
             */
            unit = ChronoUnit.DAYS;
        }
        return unit;
    }

    static boolean isSame(final Object oldValue, final Object newValue,
                          final boolean isDate,
                          final TemporalUnit unit) {

        if (Objects.isNull(oldValue) && Objects.isNull(newValue)) {
            /*
             * ( Unchanged ) When `new` and `old` are both null
             */
            return true;
        } else if (Objects.nonNull(oldValue) && Objects.nonNull(newValue)) {
            if (Types.isDate(oldValue) && isDate) {
                /*
                 * For `Date` type of `Instant`, there provide comparing method
                 * for different unit kind fo comparing.
                 * 1) Convert to instant first
                 * 2) When `unit` is null, do not comparing other kind of here.
                 */
                final Instant oldInstant = Period.parseFull(oldValue.toString())
                        .toInstant();
                final Instant newInstant = Period.parseFull(newValue.toString())
                        .toInstant();
                /*
                 * Compared by unit
                 */
                final LocalDateTime oldDateTime = Period.toDateTime(oldInstant);
                final LocalDateTime newDateTime = Period.toDateTime(newInstant);
                /*
                 * Only compared Date
                 */
                final LocalDate oldDate = oldDateTime.toLocalDate();
                final LocalDate newDate = newDateTime.toLocalDate();

                final LocalTime oldTime = oldDateTime.toLocalTime();
                final LocalTime newTime = newDateTime.toLocalTime();
                if (ChronoUnit.DAYS == unit) {
                    /*
                     * Date Only
                     */
                    return oldDate.isEqual(newDate);
                } else if (ChronoUnit.MINUTES == unit) {
                    /*
                     * Time to HH:mm
                     */
                    return oldDate.isEqual(newDate) &&
                            (oldTime.getHour() == newTime.getHour())
                            && (oldTime.getMinute() == newTime.getMinute());
                } else {
                    /*
                     * DateTime completed
                     */
                    return oldDate.isEqual(newDate) &&
                            oldTime.equals(newTime);
                }
            } else {
                /*
                 * Non date type value here
                 * Compare with `equals`
                 * Except `Date` type, we must set String literal to be compared
                 * instead of data type conversation
                 */
                return oldValue.toString().equals(newValue.toString());
            }
        } else {
            /*
             * ( Changed ) One is null, another one is not null
             * 1. Exclude situation
             * - 1) String Type,  null and "" are equal;
             *
             * They are false
             */
            return isSpecific(oldValue, newValue);
        }
    }

    private static boolean isSpecific(final Object oldValue, final Object newValue) {
        final Object select = Objects.isNull(oldValue) ? newValue : oldValue;
        if (select instanceof String) {
            final String oldStr = Objects.isNull(oldValue) ? Strings.EMPTY : oldValue.toString().trim();
            final String newStr = Objects.isNull(newValue) ? Strings.EMPTY : newValue.toString().trim();
            return oldStr.equals(newStr);
        } else return false;
    }
}
