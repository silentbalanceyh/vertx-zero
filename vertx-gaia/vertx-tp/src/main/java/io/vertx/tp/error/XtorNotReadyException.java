package io.vertx.tp.error;

import io.vertx.up.exception.UpException;

public class XtorNotReadyException extends UpException {

    public XtorNotReadyException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40034;
    }
}
