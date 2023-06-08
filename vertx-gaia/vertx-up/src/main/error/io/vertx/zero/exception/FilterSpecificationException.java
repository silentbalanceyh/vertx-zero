package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class FilterSpecificationException extends BootingException {

    public FilterSpecificationException(final Class<?> clazz,
                                        final Class<?> filterCls) {
        super(clazz, filterCls);
    }

    @Override
    public int getCode() {
        return -40052;
    }
}
