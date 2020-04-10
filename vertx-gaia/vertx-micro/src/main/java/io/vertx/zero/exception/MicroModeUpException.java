package io.vertx.zero.exception;

import io.vertx.up.exception.UpException;

public class MicroModeUpException extends UpException {

    public MicroModeUpException(final Class<?> clazz,
                                final String message) {
        super(clazz, message);
    }

    @Override
    public int getCode() {
        return -40050;
    }
}
