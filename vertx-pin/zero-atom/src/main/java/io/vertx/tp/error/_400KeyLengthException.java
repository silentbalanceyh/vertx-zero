package io.vertx.tp.error;

import io.vertx.up.exception.WebException;

public class _400KeyLengthException extends WebException {
    public _400KeyLengthException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -80527;
    }
}
