package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class VertxCallbackException extends BootingException {

    public VertxCallbackException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40003;
    }
}
