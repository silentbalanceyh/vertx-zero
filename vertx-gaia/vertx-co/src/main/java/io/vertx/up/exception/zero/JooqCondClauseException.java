package io.vertx.up.exception.zero;

import io.vertx.up.exception.UpException;

public class JooqCondClauseException extends UpException {
    public JooqCondClauseException(final Class<?> clazz, final String field,
                                   final Class<?> type, final String original) {
        super(clazz, field, type, original);
    }

    @Override
    public int getCode() {
        return -40067;
    }
}
