package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class UpClassArgsException extends BootingException {

    public UpClassArgsException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40001;
    }
}
