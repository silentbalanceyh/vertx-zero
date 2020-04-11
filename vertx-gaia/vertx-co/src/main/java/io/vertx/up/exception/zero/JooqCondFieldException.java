package io.vertx.up.exception.zero;

import io.vertx.up.exception.UpException;

public class JooqCondFieldException extends UpException {

    public JooqCondFieldException(final Class<?> clazz,
                                  final String targetField) {
        super(clazz, targetField);
    }

    @Override
    public int getCode() {
        return -40055;
    }
}
