package io.vertx.up.exception.booting;

import io.horizon.exception.BootingException;

public class JooqClassInvalidException extends BootingException {
    public JooqClassInvalidException(final Class<?> clazz, final String name) {
        super(clazz, name);
    }

    @Override
    public int getCode() {
        return -40066;
    }
}
