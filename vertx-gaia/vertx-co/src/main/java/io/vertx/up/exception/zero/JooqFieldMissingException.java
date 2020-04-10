package io.vertx.up.exception.zero;

import io.vertx.up.exception.UpException;

public class JooqFieldMissingException extends UpException {

    public JooqFieldMissingException(
            final Class<?> clazz,
            final String field,
            final Class<?> type) {
        super(clazz, field, type.getName());
    }

    @Override
    public int getCode() {
        return -40059;
    }
}
