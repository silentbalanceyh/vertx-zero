package io.vertx.up.exception.web;

import io.horizon.exception.WebException;

public class _400OpUnsupportException extends WebException {

    public _400OpUnsupportException(final Class<?> clazz,
                                    final String op) {
        super(clazz, op);
    }

    @Override
    public int getCode() {
        return -60026;
    }
}
