package io.horizon.uca.compare;

import io.modello.atom.normalize.MetaField;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface VsSame {

    static VsSame get(final Class<?> type) {
        return Objects.isNull(type) ? null : POOL_VS.POOL_SAME.getOrDefault(type, null);
    }

    static VsSame get(final MetaField type) {
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

interface POOL_VS {
    ConcurrentMap<Class<?>, VsSame> POOL_SAME = new ConcurrentHashMap<Class<?>, VsSame>() {
        {
            this.put(String.class, new VsString());
            this.put(Integer.class, new VsInteger());
            this.put(Long.class, new VsLong());
            this.put(Boolean.class, new VsBoolean());
            this.put(BigDecimal.class, new VsBigDecimal());
            this.put(LocalTime.class, new VsLocalTime());
            this.put(LocalDate.class, new VsLocalDate());
            this.put(LocalDateTime.class, new VsLocalDateTime());
            this.put(Instant.class, new VsInstant());
            this.put(JsonObject.class, new VsJsonObject());
            this.put(JsonArray.class, new VsJsonArray());
        }
    };
}

