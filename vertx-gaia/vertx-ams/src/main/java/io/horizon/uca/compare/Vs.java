package io.horizon.uca.compare;

import io.horizon.eon.VName;
import io.horizon.eon.VString;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.horizon.util.HaS;
import io.modello.atom.typed.MetaField;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;

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
    private static final Annal LOGGER = Annal.get(Vs.class);
    private static final Cc<String, Vs> CC_VS = Cc.open();

    /**
     * Here are new `typeMap` of name = HTField
     *
     * Because HTField contains `type, name, alias` and etc, it means that you can determine the format
     * Elementary / JsonObject / JsonArray by `isComplex` here.
     *
     * When format = Elementary ( Not Complex )
     *
     * 1. name is the attribute name of model ( HAttribute ).
     * 2. type is the java type that could be reflected ( stored in HTField )
     *
     * When format = JsonObject / JsonArray ( Complex )
     *
     * 1. name is the attribute name of model ( HAttribute ).
     * 2. The data type is fixed: JsonObject / JsonArray.
     * 3. And the HTField type contains `children` and isComplex = true
     */
    private transient final ConcurrentMap<String, MetaField> typeMap = new ConcurrentHashMap<>();
    /**
     * Ignored field that could be set from object.
     *
     * 1. Default ignore set
     * 2. System ignore set that could not be compared.
     * 3. The ignore fields that could be set by developer.
     */
    private transient final Set<String> ignores = new HashSet<>();

    private Vs(final ConcurrentMap<String, MetaField> mapType) {
        /*
         * this reference following rules
         * - mapType: stored current field = type
         * - mapSubType: when current field is ( JsonObject / JsonArray ), complex
         *   the mapSubType stored ( field = HTField ) and here HTField is complex type
         */
        if (Objects.nonNull(mapType) && !mapType.isEmpty()) {
            // Tpl Solution
            this.typeMap.putAll(mapType);
        }
    }

    public static Vs create(final String identifier, final ConcurrentMap<String, MetaField> mapType) {
        return CC_VS.pick(() -> new Vs(mapType), identifier);
        // Fn.po?l(POOL_VS, identifier, () -> new Vs(mapType));
    }

    // ============================ Static Comparing Change ===============================
    public static boolean isValue(final Object value, final Class<?> type) {
        // Fix Null Pointer Exception for Unknown Type comparing.
        final VsSame same = VsSame.get(type);
        return Objects.isNull(same) ? Objects.nonNull(value) : same.ok(value);
    }

    public static boolean isChange(final Object valueOld, final Object valueNew, final MetaField htField) {
        return !isSame(valueOld, valueNew, () -> VsSame.get(htField));
    }

    // ============================ Complex Comparing Change ===============================
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
                return isSame(leftValue, rightValue, () -> VsSame.get(type));
            } else {
                return Objects.isNull(leftValue) && Objects.isNull(rightValue);
            }
        } else {
            return Objects.isNull(left) && Objects.isNull(right);
        }
    }

    // ============================ Internal Comparing ===============================
    private static boolean isSame(final Object valueOld, final Object valueNew, final Supplier<VsSame> supplier) {
        if (Objects.isNull(valueOld) && Objects.isNull(valueNew)) {
            /* ( Unchanged ) When `new` and `old` are both null */
            return true;
        } else {
            /*
             * 1. type
             * 2. subset ( JsonArray Only )
             * */
            final VsSame same = supplier.get();
            if (Objects.isNull(same)) {
                final String strOld = Objects.nonNull(valueOld) ? valueOld.toString() : VString.EMPTY;
                final String strNew = Objects.nonNull(valueNew) ? valueNew.toString() : VString.EMPTY;
                return strOld.equals(strNew);
            } else {
                if (Objects.nonNull(valueOld) && Objects.nonNull(valueNew)) {
                    /*
                     * Standard workflow here
                     */
                    return same.is(valueOld, valueNew);
                } else {
                    /*
                     * Non-Standard workflow here
                     */
                    return same.isXor(valueOld, valueNew);
                }
            }
        }
    }

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

    public boolean isChange(final JsonObject previous, final JsonObject current) {
        // Copy each compared json object
        final JsonObject oldCopy = HaS.valueJObject(previous).copy();
        final JsonObject newCopy = HaS.valueJObject(current).copy();

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
        final JsonObject secure = HaS.valueJObject(twins);
        return this.isChange(secure.getJsonObject(VName.__.OLD), secure.getJsonObject(VName.__.NEW));
    }

    public boolean isChange(final Object valueOld, final Object valueNew, final String attribute) {
        final MetaField fieldType = this.typeMap.getOrDefault(attribute, null);
        final boolean isChanged = isChange(valueOld, valueNew, fieldType);
        LOGGER.info("Field compared: name = {0}, type = {1}, result = {2}",
            attribute, fieldType.type(), isChanged);
        return isChanged;
    }

    public boolean isValue(final Object value, final String attribute) {
        final MetaField fieldType = this.typeMap.getOrDefault(attribute, null);
        return isValue(value, fieldType.type());
    }
}
