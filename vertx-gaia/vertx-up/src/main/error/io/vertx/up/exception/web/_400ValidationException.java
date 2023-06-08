package io.vertx.up.exception.web;

import io.horizon.exception.WebException;

import java.lang.reflect.Method;

public class _400ValidationException extends WebException {

    public _400ValidationException(final Class<?> clazz,
                                   final Class<?> target,
                                   final Method method,
                                   final String info) {
        super(clazz, target, method, info);
    }

    @Override
    public int getCode() {
        return -60000;
    }
}
