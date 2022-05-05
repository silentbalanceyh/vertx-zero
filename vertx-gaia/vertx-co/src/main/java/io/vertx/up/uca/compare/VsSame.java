package io.vertx.up.uca.compare;

import io.vertx.up.experiment.mixture.HTField;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface VsSame {

    static VsSame get(final Class<?> type) {
        return Objects.isNull(type) ? null : Pool.POOL_SAME.getOrDefault(type, null);
    }

    static VsSame get(final HTField type) {
        final VsSame same = get(type.type());
        if (Objects.isNull(same)) {
            return null;
        }
        return ((AbstractSame) same).bind(type);
    }

    /**
     * Comparing
     *
     * 1. Internal Vs
     * 2. Extension VsPlugin -> Adjuster ( For Extension Here )
     *
     * @param valueOld {@link Object} input old
     * @param valueNew {@link Object} input new
     *
     * @return {@link Boolean} true when Same.
     */
    boolean is(Object valueOld, Object valueNew);

    /**
     * Check value is ok
     *
     * @param value {@link Object}
     *
     * @return {@link Boolean}
     */
    boolean ok(Object value);

    /**
     * 1. Any one input is `null`
     * 2. Both are not be null at the same time.
     *
     * @param valueOld {@link Object} input old
     * @param valueNew {@link Object} input new
     *
     * @return {@link Boolean} true when Same.
     */
    default boolean isXor(final Object valueOld, final Object valueNew) {
        return false;
    }
}
