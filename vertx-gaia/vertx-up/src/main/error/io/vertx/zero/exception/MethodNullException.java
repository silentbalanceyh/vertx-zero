package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class MethodNullException extends BootingException {

    public MethodNullException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40007;
    }
}
