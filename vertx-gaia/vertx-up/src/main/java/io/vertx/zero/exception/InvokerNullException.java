package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class InvokerNullException extends BootingException {

    public InvokerNullException(final Class<?> target,
                                final Class<?> returnType,
                                final Class<?> paramType) {
        super(target, returnType, paramType);
    }

    @Override
    public int getCode() {
        return -40047;
    }
}
