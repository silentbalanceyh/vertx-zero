package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class IndexExceedException extends BootingException {

    public IndexExceedException(final Class<?> clazz,
                                final Integer index) {
        super(clazz, String.valueOf(index));
    }

    @Override
    public int getCode() {
        return -40032;
    }
}
