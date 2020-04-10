package io.vertx.up.exception.zero;

import io.vertx.up.exception.UpException;

public class EtcdConfigEmptyException extends UpException {

    public EtcdConfigEmptyException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40027;
    }
}
