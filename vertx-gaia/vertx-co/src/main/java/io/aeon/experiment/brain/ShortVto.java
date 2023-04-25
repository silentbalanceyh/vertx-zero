package io.aeon.experiment.brain;

import io.vertx.up.eon.bridge.Values;
import io.vertx.up.util.Ut;

import java.math.BigDecimal;
import java.util.Objects;

public class ShortVto implements Vto<Short> {

    @Override
    public Short to(final Object value, final Class<?> type) {
        if (Objects.isNull(value)) {
            /*
             * -1 as default
             */
            return (short) Values.RANGE;
        } else {
            if (Short.class == type) {
                /*
                 * Default
                 */
                return (Short) value;
            } else if (String.class == type) {
                /*
                 * String -> Short
                 */
                return Ut.isInteger(value.toString()) ?
                    Short.parseShort(value.toString()) :
                    (short) Values.RANGE;
            } else if (Tool.isInteger(type)) {
                /*
                 * Integer -> Short
                 * Long -> Short
                 */
                return Short.parseShort(value.toString());
            } else if (Tool.isDecimal(type)) {
                /*
                 * Double -> Short
                 * Float -> Short
                 */
                final Double normalized = Double.parseDouble(value.toString());
                return normalized.shortValue();
            } else if (BigDecimal.class == type) {

                return ((BigDecimal) value).shortValue();
            } else if (Boolean.class == type) {
                /*
                 * Boolean -> Short
                 */
                final Boolean normalized = (Boolean) value;
                if (normalized) {
                    return (short) Values.ONE;
                } else {
                    return (short) Values.ZERO;
                }
            }
        }
        return null;
    }
}
