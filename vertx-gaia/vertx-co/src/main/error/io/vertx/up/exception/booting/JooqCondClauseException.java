package io.vertx.up.exception.booting;

import io.horizon.exception.BootingException;

public class JooqCondClauseException extends BootingException {
    public JooqCondClauseException(final Class<?> clazz, final String field,
                                   final Class<?> type, final String original) {
        super(clazz, field, type, original);
    }

    @Override
    public int getCode() {
        return -40067;
    }
}
