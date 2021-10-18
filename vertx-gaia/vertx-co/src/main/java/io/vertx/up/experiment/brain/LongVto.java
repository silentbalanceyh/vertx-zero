package io.vertx.up.experiment.brain;

import io.vertx.up.eon.Values;
import io.vertx.up.util.Ut;

import java.math.BigDecimal;
import java.util.Objects;

public class LongVto implements Vto<Long> {

    @Override
    public Long to(final Object value, final Class<?> type) {
        if (Objects.isNull(value)) {
            /*
             * -1 as default
             */
            return (long) Values.RANGE;
        } else {
            if (Long.class == type) {
                /*
                 * Default
                 */
                return (Long) value;
            } else if (String.class == type) {
                /*
                 * String -> Long
                 */
                return Ut.isInteger(value.toString()) ?
                    Long.parseLong(value.toString()) :
                    Values.RANGE;
            } else if (Tool.isInteger(type)) {
                /*
                 * Integer -> Long
                 * Short -> Long
                 */
                return Long.parseLong(value.toString());
            } else if (Tool.isDecimal(type)) {
                /*
                 * Double -> Long
                 * Float -> Long
                 */
                final Double normalized = Double.parseDouble(value.toString());
                return normalized.longValue();
            } else if (BigDecimal.class == type) {

                return ((BigDecimal) value).longValue();
            } else if (Boolean.class == type) {
                /*
                 * Boolean -> Long
                 */
                final Boolean normalized = (Boolean) value;
                if (normalized) {
                    return (long) Values.ONE;
                } else {
                    return (long) Values.ZERO;
                }
            }
        }
        return null;
    }
}
