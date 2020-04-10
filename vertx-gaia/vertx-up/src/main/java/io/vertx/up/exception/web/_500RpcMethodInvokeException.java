package io.vertx.up.exception.web;

import io.vertx.up.exception.WebException;

public class _500RpcMethodInvokeException extends WebException {

    public _500RpcMethodInvokeException(final Class<?> clazz,
                                        final Object returnValue) {
        super(clazz, clazz, returnValue);
    }

    @Override
    public int getCode() {
        return -60028;
    }
}
