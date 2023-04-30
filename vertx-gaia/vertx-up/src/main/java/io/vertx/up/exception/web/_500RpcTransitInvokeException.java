package io.vertx.up.exception.web;

import io.horizon.exception.WebException;

import java.lang.reflect.Method;

public class _500RpcTransitInvokeException extends WebException {

    public _500RpcTransitInvokeException(final Class<?> clazz,
                                         final Method method,
                                         final Throwable ex) {
        super(clazz, clazz.getName(), method, ex.getMessage());
    }

    @Override
    public int getCode() {
        return -60036;
    }
}
