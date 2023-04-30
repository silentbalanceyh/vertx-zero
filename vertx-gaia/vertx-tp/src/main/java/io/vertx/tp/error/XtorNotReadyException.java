package io.vertx.tp.error;

import io.horizon.exception.BootingException;

public class XtorNotReadyException extends BootingException {

    public XtorNotReadyException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40034;
    }
}
