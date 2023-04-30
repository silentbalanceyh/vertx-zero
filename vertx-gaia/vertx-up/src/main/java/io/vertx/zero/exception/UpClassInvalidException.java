package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class UpClassInvalidException extends BootingException {

    public UpClassInvalidException(final Class<?> clazz,
                                   final String className) {
        super(clazz, className);
    }

    @Override
    public int getCode() {
        return -40002;
    }
}
