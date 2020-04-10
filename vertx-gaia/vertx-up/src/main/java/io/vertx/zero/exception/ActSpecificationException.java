package io.vertx.zero.exception;

import io.vertx.up.exception.UpException;

public class ActSpecificationException extends UpException {

    public ActSpecificationException(final Class<?> clazz,
                                     final Boolean isBatch) {
        super(clazz, isBatch);
    }

    @Override
    public int getCode() {
        return -40064;
    }
}
