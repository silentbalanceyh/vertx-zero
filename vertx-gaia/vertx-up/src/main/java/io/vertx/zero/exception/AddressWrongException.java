package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

import java.lang.reflect.Method;

public class AddressWrongException extends BootingException {

    public AddressWrongException(final Class<?> clazz,
                                 final String address,
                                 final Class<?> target,
                                 final Method method) {
        super(clazz, address, target.getName(), method.getName());
    }

    @Override
    public int getCode() {
        return -40012;
    }
}
