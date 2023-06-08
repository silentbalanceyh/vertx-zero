package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

import java.lang.reflect.Method;

public class WorkerConflictException extends BootingException {

    public WorkerConflictException(final Class<?> clazz,
                                   final Method method) {
        super(clazz, method.getName(), method.getDeclaringClass());
    }

    @Override
    public int getCode() {
        return -40049;
    }
}
