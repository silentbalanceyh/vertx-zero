package io.vertx.up.exception.web;

import io.horizon.exception.WebException;

public class _400ParameterFromStringException extends WebException {

    public _400ParameterFromStringException(final Class<?> clazz,
                                            final Class<?> expectedType,
                                            final String literal) {
        super(clazz, literal, expectedType);
    }

    @Override
    public int getCode() {
        return -60004;
    }
}
