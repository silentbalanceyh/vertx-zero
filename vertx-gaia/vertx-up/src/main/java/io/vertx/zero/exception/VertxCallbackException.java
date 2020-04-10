package io.vertx.zero.exception;

import io.vertx.up.exception.UpException;

public class VertxCallbackException extends UpException {

    public VertxCallbackException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40003;
    }
}
