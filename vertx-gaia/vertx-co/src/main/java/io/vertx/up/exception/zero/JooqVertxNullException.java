package io.vertx.up.exception.zero;

import io.vertx.up.exception.UpException;

public class JooqVertxNullException extends UpException {

    public JooqVertxNullException(
        final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40060;
    }
}
