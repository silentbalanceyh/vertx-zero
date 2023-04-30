package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class FilterOrderException extends BootingException {

    public FilterOrderException(final Class<?> clazz,
                                final Class<?> filterCls) {
        super(clazz, filterCls);
    }

    @Override
    public int getCode() {
        return -40053;
    }
}
