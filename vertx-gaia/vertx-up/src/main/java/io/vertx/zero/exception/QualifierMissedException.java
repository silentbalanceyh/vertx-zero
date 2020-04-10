package io.vertx.zero.exception;

import io.vertx.up.exception.UpException;

public class QualifierMissedException extends UpException {

    public QualifierMissedException(final Class<?> clazz,
                                    final String field,
                                    final String className) {
        super(clazz, field, className);
    }

    @Override
    public int getCode() {
        return -40023;
    }
}
