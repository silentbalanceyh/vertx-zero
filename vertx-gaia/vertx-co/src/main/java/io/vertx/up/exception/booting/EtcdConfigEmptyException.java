package io.vertx.up.exception.booting;

import io.horizon.exception.BootingException;

public class EtcdConfigEmptyException extends BootingException {

    public EtcdConfigEmptyException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40027;
    }
}
