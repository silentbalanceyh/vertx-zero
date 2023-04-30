package io.vertx.up.exception.web;

import io.horizon.exception.WebException;

public class _400ResourceBuildingException extends WebException {

    public _400ResourceBuildingException(final Class<?> clazz,
                                         final Object reference) {
        super(clazz, reference.toString());
    }

    @Override
    public int getCode() {
        return -60001;
    }
}
