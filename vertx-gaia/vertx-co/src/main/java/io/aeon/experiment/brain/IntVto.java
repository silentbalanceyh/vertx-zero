package io.aeon.experiment.brain;

import io.horizon.eon.VValue;
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
            return VValue.RANGE;
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
                    VValue.RANGE;
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
                    return VValue.ONE;
                } else {
                    return VValue.ZERO;
                }
            }
        }
        return null;
    }
}