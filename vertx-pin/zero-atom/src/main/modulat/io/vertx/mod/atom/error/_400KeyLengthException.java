package io.vertx.mod.atom.error;

import io.horizon.exception.WebException;

public class _400KeyLengthException extends WebException {
    public _400KeyLengthException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -80527;
    }
}
