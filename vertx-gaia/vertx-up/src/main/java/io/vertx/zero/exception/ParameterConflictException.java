package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class ParameterConflictException extends BootingException {

    public ParameterConflictException(final Class<?> clazz,
                                      final Class<?> target) {
        super(clazz, target);
    }

    @Override
    public int getCode() {
        return -40011;
    }
}
