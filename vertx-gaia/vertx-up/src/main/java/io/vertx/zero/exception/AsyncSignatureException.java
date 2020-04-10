package io.vertx.zero.exception;

import io.vertx.up.exception.UpException;

public class AsyncSignatureException extends UpException {

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
