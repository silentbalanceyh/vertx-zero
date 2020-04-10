package io.vertx.zero.exception;

import io.vertx.up.exception.UpException;

public class FilterContextException extends UpException {

    public FilterContextException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40051;
    }
}
