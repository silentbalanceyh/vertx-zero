package io.vertx.up.exception.boot;

import io.horizon.exception.BootingException;

public class XtorConnectException extends BootingException {

    public XtorConnectException(final Class<?> clazz,
                                final String component,
                                final String method) {
        super(clazz, component, method);
    }

    @Override
    public int getCode() {
        return -40033;
    }
}
