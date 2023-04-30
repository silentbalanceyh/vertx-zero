package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class PathAnnoEmptyException extends BootingException {

    public PathAnnoEmptyException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40006;
    }
}
