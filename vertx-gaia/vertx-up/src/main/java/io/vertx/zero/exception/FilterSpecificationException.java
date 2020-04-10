package io.vertx.zero.exception;

import io.vertx.up.exception.UpException;

public class FilterSpecificationException extends UpException {

    public FilterSpecificationException(final Class<?> clazz,
                                        final Class<?> filterCls) {
        super(clazz, filterCls);
    }

    @Override
    public int getCode() {
        return -40052;
    }
}
