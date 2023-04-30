package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class QualifierMissedException extends BootingException {

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
