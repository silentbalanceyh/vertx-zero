package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class FilterContextException extends BootingException {

    public FilterContextException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40051;
    }
}
