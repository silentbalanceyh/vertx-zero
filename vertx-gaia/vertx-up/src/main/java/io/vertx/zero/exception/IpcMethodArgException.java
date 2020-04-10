package io.vertx.zero.exception;

import io.vertx.up.exception.UpException;

import java.lang.reflect.Method;

public class IpcMethodArgException extends UpException {

    public IpcMethodArgException(final Class<?> clazz,
                                 final Method method) {
        super(clazz, method);
    }

    @Override
    public int getCode() {
        return -40046;
    }
}
