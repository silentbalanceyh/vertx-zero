package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class MicroModeUpException extends BootingException {

    public MicroModeUpException(final Class<?> clazz,
                                final String message) {
        super(clazz, message);
    }

    @Override
    public int getCode() {
        return -40050;
    }
}
