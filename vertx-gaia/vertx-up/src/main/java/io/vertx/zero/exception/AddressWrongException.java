package io.vertx.zero.exception;

import io.vertx.up.exception.UpException;

import java.lang.reflect.Method;

public class AddressWrongException extends UpException {

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
