package io.vertx.zero.exception;

import io.vertx.up.exception.UpException;

public class FilterOrderException extends UpException {

    public FilterOrderException(final Class<?> clazz,
                                final Class<?> filterCls) {
        super(clazz, filterCls);
    }

    @Override
    public int getCode() {
        return -40053;
    }
}
