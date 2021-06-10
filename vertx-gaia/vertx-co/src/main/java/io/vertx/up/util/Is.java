package io.vertx.up.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.compare.Vary;
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
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

/*
 * Specific checking
 */
@SuppressWarnings("all")
class Is {

    static boolean isSameBy(final Object left, final Object right, final String field) {
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
                        return isSameBy(leftValue, rightValue, field);
                    } else {
                        return left.equals(right);
                    }
                }
            }
        }
    }

    static <T, V> Boolean isSame(final T left, final T right, final Function<T, V> fnGet) {
        if (Objects.nonNull(left) && Objects.nonNull(right)) {
            final V leftValue = fnGet.apply(left);
            final V rightValue = fnGet.apply(right);
            return isSame(leftValue, rightValue);
        } else {
            return Objects.isNull(left) && Objects.isNull(right);
        }
    }

    static <T> Boolean isSame(final T left, final T right) {
        if (Objects.nonNull(left) && Objects.nonNull(right)) {
            final Class<?> clazz = left.getClass();
            return isSame(left, right, clazz, new HashSet<>());
        } else {
            return Objects.isNull(left) && Objects.isNull(right);
        }
    }

    static boolean isChanged(final Vary param,
                             final BiFunction<String, Class<?>, BiPredicate<Object, Object>> fnPredicate) {
        final Set<String> ignores = param.ignores();
        /*
         * copy each compared json object and remove
         * all fields that will not be compared here.
         */
        final JsonObject oldCopy = param.original().copy();
        final JsonObject newCopy = param.current().copy();
        if (Objects.nonNull(ignores) && !ignores.isEmpty()) {
            ignores.forEach(oldCopy::remove);
            ignores.forEach(newCopy::remove);
        }
        final Function<String, Boolean> isSame = (field) -> {
            /*
             * Extract value from each record
             */
            final Object oldValue = oldCopy.getValue(field);
            final Object newValue = newCopy.getValue(field);
            final Class<?> type = param.type(field);
            final boolean basic = isSame(oldValue, newValue, type, param.diff(field));
            if (basic) {
                return Boolean.TRUE;
            } else {
                if (Objects.isNull(fnPredicate)) {
                    return Boolean.FALSE;
                } else {
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

    static boolean isSame(final Object oldValue, final Object newValue,
                          final Class<?> clazz, final Set<String> diffSet) {
        if (Objects.isNull(oldValue) && Objects.isNull(newValue)) {
            /*
             * ( Unchanged ) When `new` and `old` are both null
             */
            return true;
        } else if (Objects.nonNull(oldValue) && Objects.nonNull(newValue)) {
            if (Types.isDate(oldValue) || Types.isDate(clazz)) {
                /*
                 * Date
                 */
                return isSameDate(oldValue, newValue, clazz);
            } else if (Types.isJArray(oldValue) || Types.isJArray(clazz)) {
                /*
                 * Array with configuration of diff
                 */
                return isSameArray((JsonArray) oldValue, (JsonArray) newValue, diffSet);
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

    private static boolean isSameArray(final JsonArray oldValue, final JsonArray newValue, final Set<String> diffSet) {
        if (oldValue.size() == newValue.size()) {
            /*
             * size is the same
             */
            return Ut.itJArray(oldValue).allMatch(original -> isIn(newValue, original, diffSet));
        } else {
            /*
             * size is different, not the same ( Fast Checking )
             */
            return false;
        }
    }

    private static boolean isIn(final JsonArray source, final JsonObject value, final Set<String> diffSet) {
        return Ut.itJArray(source).anyMatch(item -> {
            final JsonObject checked = ArrayL.subset(item, diffSet);
            final JsonObject pending = ArrayL.subset(value, diffSet);
            return checked.equals(pending);
        });
    }

    private static boolean isSameDate(final Object oldValue, final Object newValue, final Class<?> clazz) {
        /*
         * Time Unit Calculation
         */
        final TemporalUnit unit = getUnit(clazz);
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
    }

    private static TemporalUnit getUnit(final Class<?> clazz) {
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

    private static boolean isSpecific(final Object oldValue, final Object newValue) {
        final Object select = Objects.isNull(oldValue) ? newValue : oldValue;
        if (select instanceof String) {
            final String oldStr = Objects.isNull(oldValue) ? Strings.EMPTY : oldValue.toString().trim();
            final String newStr = Objects.isNull(newValue) ? Strings.EMPTY : newValue.toString().trim();
            return oldStr.equals(newStr);
        } else return false;
    }
}
