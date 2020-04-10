package io.vertx.zero.exception;

import io.vertx.up.exception.UpException;

import java.lang.reflect.Method;

public class IpcMethodTargetException extends UpException {

    public IpcMethodTargetException(final Class<?> clazz,
                                    final Method method,
                                    final String to,
                                    final String name) {
        super(clazz, method, to, name);
    }

    @Override
    public int getCode() {
        return -40043;
    }
}
