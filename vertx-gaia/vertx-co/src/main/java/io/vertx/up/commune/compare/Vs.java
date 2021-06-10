package io.vertx.up.commune.compare;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * ## Diff Container ( New Version )
 *
 * ### 1. Intro
 *
 * Store definition of each model for diff ( comparing workflow )
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Vs implements Serializable {

    private static final ConcurrentMap<String, Vs> POOL_VS = new ConcurrentHashMap<>();
    /**
     * Attribute Stored
     * name = type
     *
     * 1. name is the attribute name of model ( `M_ATTRIBUTE` ).
     * 2. type is the java type that could be reflect.
     */
    private transient final ConcurrentMap<String, Class<?>> mapType = new ConcurrentHashMap<>();

    /**
     * Subset Attribute Stored
     * name = Set
     *
     * 1. name is the attribute name of model ( `M_ATTRIBUTE` ).
     * 2. The data type is fixed: JsonObject / JsonArray.
     * 3. The `Set` means the attribute names of JsonObject or JsonArray here.
     */
    private transient final ConcurrentMap<String, Set<String>> mapSubtype = new ConcurrentHashMap<>();
    /**
     * Ignored field that could be set from object.
     *
     * 1. Default ignore set
     * 2. System ignore set that could not be compared.
     * 3. The ignore fields that could be set by developer.
     */
    private transient final Set<String> ignores = new HashSet<>();

    @Fluent
    public Vs ignores(final Set<String> ignoreSet) {
        if (Objects.nonNull(ignoreSet) && !ignoreSet.isEmpty()) {
            this.ignores.clear();
            this.ignores.addAll(ignoreSet);
        }
        return this;
    }

    public Set<String> ignores() {
        return this.ignores;
    }

    private Vs(final ConcurrentMap<String, Class<?>> mapType, final ConcurrentMap<String, Set<String>> mapSubtype) {
        if (Objects.nonNull(mapType) && !mapType.isEmpty()) {
            this.mapType.clear();
            this.mapType.putAll(mapType);
        }
        if (Objects.nonNull(mapSubtype) && !mapSubtype.isEmpty()) {
            this.mapSubtype.clear();
            this.mapSubtype.putAll(mapSubtype);
        }
    }

    public static Vs create(final String identifier, final ConcurrentMap<String, Class<?>> mapType, final ConcurrentMap<String, Set<String>> mapSubtype) {
        return Fn.pool(POOL_VS, identifier, () -> new Vs(mapType, mapSubtype));
    }

    // ============================ Complex Comparing Change ===============================

    public boolean isChange(final JsonObject previous, final JsonObject current) {
        // Copy each compared json object
        final JsonObject oldCopy = Ut.sureJObject(previous).copy();
        final JsonObject newCopy = Ut.sureJObject(current).copy();

        /*
         * Remove ignore fields, iterating once only, old code ( Twice iterating )
         * 1. ignores.forEach(oldCopy::remove);
         * 2. ignores.forEach(newCopy::remove);
         */
        if (!this.ignores.isEmpty()) {
            this.ignores.forEach(field -> {
                oldCopy.remove(field);
                newCopy.remove(field);
            });
        }

        /*
         * Function building here to call `VsInternal`
         */
        final Function<String, Boolean> isSame = field -> {
            /*
             * Extract value here
             */
            final Object valueOld = oldCopy.getValue(field);
            final Object valueNew = newCopy.getValue(field);
            return !this.isChange(valueOld, valueNew, field);
        };
        /*
         * Get the final result of calculation.
         * 1) From old Re-calculation
         * 2) From new Re-calculation
         */
        final boolean unchanged = oldCopy.fieldNames().stream().allMatch(isSame::apply);
        final Set<String> newLefts = new HashSet<>(newCopy.fieldNames());
        newLefts.removeAll(oldCopy.fieldNames());
        final boolean additional = newLefts.stream().allMatch(isSame::apply);
        return !(unchanged && additional);
    }

    public boolean isChange(final JsonObject twins) {
        final JsonObject secure = Ut.sureJObject(twins);
        return this.isChange(secure.getJsonObject(Values.VS.OLD), secure.getJsonObject(Values.VS.NEW));
    }

    public boolean isChange(final Object valueOld, final Object valueNew, final String attribute) {
        final Class<?> type = this.mapType.get(attribute);
        final Set<String> subset = this.mapSubtype.getOrDefault(attribute, new HashSet<>());
        return isChange(valueOld, valueNew, type, subset);
    }

    // ============================ Static Comparing Change ===============================
    public static boolean isChange(final Object valueOld, final Object valueNew, final Class<?> type, final Set<String> subset) {
        return !isSame(valueOld, valueNew, type, subset);
    }

    public static boolean isSame(final Object valueOld, final Object valueNew, final Class<?> type, final Set<String> subset) {
        if (Objects.isNull(valueOld) && Objects.isNull(valueNew)) {
            /* ( Unchanged ) When `new` and `old` are both null */
            return true;
        } else {
            /*
             * 1. type
             * 2. subset ( JsonArray Only )
             * */
            final VsSame same = Objects.requireNonNull(VsSame.get(type)).bind(subset);
            assert Objects.nonNull(same) : "Here the VsSame could not be null";
            if (Objects.nonNull(valueOld) && Objects.nonNull(valueNew)) {
                /*
                 * Standard workflow here
                 */
                return same.is(valueOld, valueNew);
            } else {
                /*
                 * Non Standard workflow here
                 */
                return same.isOr(valueOld, valueNew);
            }
        }
    }

    public static <T, V> boolean isChange(final T left, final T right, final Function<T, V> fnGet) {
        return !isSame(left, right, fnGet);
    }

    public static <T, V> boolean isSame(final T left, final T right, final Function<T, V> fnGet) {
        if (Objects.nonNull(left) && Objects.nonNull(right)) {
            // Both are not null
            final V leftValue = fnGet.apply(left);
            final V rightValue = fnGet.apply(right);
            if (Objects.nonNull(leftValue) && Objects.nonNull(rightValue)) {
                final Class<?> type = leftValue.getClass();
                return isSame(leftValue, rightValue, type, new HashSet<>());
            } else {
                return Objects.isNull(leftValue) && Objects.isNull(rightValue);
            }
        } else {
            return Objects.isNull(left) && Objects.isNull(right);
        }
    }
}
