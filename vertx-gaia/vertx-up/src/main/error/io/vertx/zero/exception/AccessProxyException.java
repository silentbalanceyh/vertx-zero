package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class AccessProxyException extends BootingException {

    public AccessProxyException(final Class<?> clazz,
                                final Class<?> target) {
        super(clazz, target.getName());
    }

    @Override
    public int getCode() {
        return -40010;
    }
}
