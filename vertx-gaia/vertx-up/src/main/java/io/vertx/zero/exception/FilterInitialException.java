package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class FilterInitialException extends BootingException {

    public FilterInitialException(final Class<?> clazz,
                                  final Class<?> filterCls) {
        super(clazz, filterCls);
    }

    @Override
    public int getCode() {
        return -40054;
    }
}
