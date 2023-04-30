package io.horizon.uca.convert;

import java.util.Objects;

public class BooleanVto implements Vto<Boolean> {

    @Override
    public Boolean to(final Object value, final Class<?> type) {
        if (Objects.isNull(value)) {
            return Boolean.FALSE;
        } else {
            final String literal = value.toString();
            return Boolean.valueOf(literal);
        }
    }
}
