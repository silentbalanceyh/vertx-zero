package io.vertx.zero.exception;

import io.vertx.up.exception.UpException;

import java.lang.reflect.Method;

public class ReturnTypeException extends UpException {

    public ReturnTypeException(final Class<?> clazz,
                               final Method method) {
        super(clazz, method.getName(), method.getDeclaringClass().getName());
    }

    @Override
    public int getCode() {
        return -40013;
    }
}
