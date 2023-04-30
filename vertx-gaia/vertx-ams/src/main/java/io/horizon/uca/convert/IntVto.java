package io.horizon.uca.convert;

import io.horizon.eon.VValue;
import io.horizon.util.HaS;

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
                return HaS.isInteger(value.toString()) ?
                    Integer.parseInt(value.toString()) :
                    VValue.RANGE;
            } else if (HaS.isInteger(type)) {
                /*
                 * Long -> Integer
                 * Short -> Integer
                 */
                return Integer.parseInt(value.toString());
            } else if (HaS.isDecimal(type)) {
                /*
                 * Double -> Integer
                 * Float -> Integer
                 */
                final double normalized = Double.parseDouble(value.toString());
                return (int) normalized;
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
