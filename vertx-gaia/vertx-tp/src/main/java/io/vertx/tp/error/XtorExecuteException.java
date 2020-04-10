package io.vertx.tp.error;

import io.vertx.up.exception.UpException;

public class XtorExecuteException extends UpException {

    public XtorExecuteException(final Class<?> clazz,
                                final String details) {
        super(clazz, details);
    }

    @Override
    public int getCode() {
        return -40035;
    }
}
