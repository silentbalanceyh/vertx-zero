package io.vertx.zero.exception;

import io.vertx.up.exception.UpException;

import java.lang.reflect.Method;

public class WorkerConflictException extends UpException {

    public WorkerConflictException(final Class<?> clazz,
                                   final Method method) {
        super(clazz, method.getName(), method.getDeclaringClass());
    }

    @Override
    public int getCode() {
        return -40049;
    }
}
