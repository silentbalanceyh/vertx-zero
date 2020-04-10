package io.vertx.up.exception.zero;

import io.vertx.up.exception.UpException;

public class JooqClassInvalidException extends UpException {
    public JooqClassInvalidException(final Class<?> clazz, final String name) {
        super(clazz, name);
    }

    @Override
    public int getCode() {
        return -40066;
    }
}
