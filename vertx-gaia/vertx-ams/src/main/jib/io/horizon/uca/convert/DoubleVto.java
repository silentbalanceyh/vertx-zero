package io.horizon.uca.convert;

import io.horizon.util.HUt;

import java.math.BigDecimal;
import java.util.Objects;

public class DoubleVto implements Vto<Double> {

    @Override
    public Double to(final Object value, final Class<?> type) {
        if (Objects.isNull(value)) {
            return -1.0;
        } else {
            if (Double.class == type) {

                return (Double) value;
            } else if (String.class == type) {

                return HUt.isDecimal(value.toString()) || HUt.isInteger(value.toString()) ? Double.parseDouble(value.toString()) : -1.0;
            } else if (HUt.isInteger(type) || HUt.isDecimal(type)) {

                return Double.parseDouble(value.toString());
            } else if (BigDecimal.class == type) {

                return ((BigDecimal) value).doubleValue();
            } else if (Boolean.class == type) {
                final Boolean normalized = (Boolean) value;
                if (normalized) {
                    return 1.0;
                } else {
                    return 0.0;
                }
            }
        }
        return null;
    }
}
