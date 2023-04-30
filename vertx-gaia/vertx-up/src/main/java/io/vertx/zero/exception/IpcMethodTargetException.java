package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

import java.lang.reflect.Method;

public class IpcMethodTargetException extends BootingException {

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
