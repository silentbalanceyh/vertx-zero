package io.vertx.up.experiment.brain;

import io.vertx.up.eon.Values;
import io.vertx.up.util.Ut;

import java.math.BigDecimal;
import java.util.Objects;

public class IntVto implements Vto<Integer> {

    @Override
    public Integer to(final Object value, final Class<?> type) {
        if (Objects.isNull(value)) {
            /*
             * -1 as default
             */
            return Values.RANGE;
        } else {
            if (Integer.class == type) {
                /*
                 * Default
                 */
                return (Integer) value;
            } else if (String.class == type) {
                /*
                 * String -> Integer
                 */
                return Ut.isInteger(value.toString()) ?
                    Integer.parseInt(value.toString()) :
                    Values.RANGE;
            } else if (Tool.isInteger(type)) {
                /*
                 * Long -> Integer
                 * Short -> Integer
                 */
                return Integer.parseInt(value.toString());
            } else if (Tool.isDecimal(type)) {
                /*
                 * Double -> Integer
                 * Float -> Integer
                 */
                final Double normalized = Double.parseDouble(value.toString());
                return normalized.intValue();
            } else if (BigDecimal.class == type) {

                return ((BigDecimal) value).intValue();
            } else if (Boolean.class == type) {
                /*
                 * Boolean -> Integer
                 */
                final Boolean normalized = (Boolean) value;
                if (normalized) {
                    return Values.ONE;
                } else {
                    return Values.ZERO;
                }
            }
        }
        return null;
    }
}
