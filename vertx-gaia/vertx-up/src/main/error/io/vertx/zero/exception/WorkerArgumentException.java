package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

import java.lang.reflect.Method;

public class WorkerArgumentException extends BootingException {

    public WorkerArgumentException(final Class<?> clazz,
                                   final Method method) {
        super(clazz, method.getName(), method.getDeclaringClass().getName());
    }

    @Override
    public int getCode() {
        return -40017;
    }
}
