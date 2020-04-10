package io.vertx.up.exception.zero;

import io.vertx.up.exception.UpException;

public class DuplicatedImplException extends UpException {

    public DuplicatedImplException(final Class<?> clazz,
                                   final Class<?> interfaceCls) {
        super(clazz, interfaceCls.getName());
    }

    @Override
    public int getCode() {
        return -40028;
    }
}
