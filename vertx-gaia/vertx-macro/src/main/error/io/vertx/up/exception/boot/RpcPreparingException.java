package io.vertx.up.exception.boot;

import io.horizon.exception.BootingException;

public class RpcPreparingException extends BootingException {

    public RpcPreparingException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40037;
    }
}
