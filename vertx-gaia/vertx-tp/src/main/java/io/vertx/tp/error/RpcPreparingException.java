package io.vertx.tp.error;

import io.vertx.up.exception.UpException;

public class RpcPreparingException extends UpException {

    public RpcPreparingException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40037;
    }
}
