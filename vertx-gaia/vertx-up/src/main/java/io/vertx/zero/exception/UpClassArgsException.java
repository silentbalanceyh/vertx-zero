package io.vertx.zero.exception;

import io.vertx.up.exception.UpException;

public class UpClassArgsException extends UpException {

    public UpClassArgsException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40001;
    }
}
