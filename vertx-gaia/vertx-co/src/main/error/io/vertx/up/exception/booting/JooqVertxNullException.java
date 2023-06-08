package io.vertx.up.exception.booting;

import io.horizon.exception.BootingException;

public class JooqVertxNullException extends BootingException {

    public JooqVertxNullException(
        final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40060;
    }
}
