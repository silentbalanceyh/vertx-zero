package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class ActSpecificationException extends BootingException {

    public ActSpecificationException(final Class<?> clazz,
                                     final Boolean isBatch) {
        super(clazz, isBatch);
    }

    @Override
    public int getCode() {
        return -40064;
    }
}
