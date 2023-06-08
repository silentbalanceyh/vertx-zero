package io.vertx.up.exception.booting;

import io.horizon.exception.BootingException;

import java.lang.reflect.Method;

public class InvokingSpecException extends BootingException {

    public InvokingSpecException(final Class<?> clazz,
                                 final Method method) {
        super(clazz, method.getName());
    }

    @Override
    public int getCode() {
        return -40063;
    }
}
