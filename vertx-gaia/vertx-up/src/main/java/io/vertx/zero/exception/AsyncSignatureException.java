package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class AsyncSignatureException extends BootingException {

    public AsyncSignatureException(final Class<?> clazz,
                                   final String returnType,
                                   final String paramType) {
        super(clazz, returnType, paramType);
    }

    @Override
    public int getCode() {
        return -40018;
    }
}
