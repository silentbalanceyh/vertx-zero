package io.vertx.zero.exception;

import io.vertx.up.exception.UpException;

public class PathAnnoEmptyException extends UpException {

    public PathAnnoEmptyException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40006;
    }
}
