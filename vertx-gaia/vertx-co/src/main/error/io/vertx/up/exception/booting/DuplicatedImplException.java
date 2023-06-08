package io.vertx.up.exception.booting;

import io.horizon.exception.BootingException;

public class DuplicatedImplException extends BootingException {

    public DuplicatedImplException(final Class<?> clazz,
                                   final Class<?> interfaceCls) {
        super(clazz, interfaceCls.getName());
    }

    @Override
    public int getCode() {
        return -40028;
    }
}
