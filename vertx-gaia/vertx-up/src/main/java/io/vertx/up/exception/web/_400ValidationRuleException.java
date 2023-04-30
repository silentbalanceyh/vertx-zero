package io.vertx.up.exception.web;

import io.horizon.exception.WebException;

public class _400ValidationRuleException extends WebException {

    public _400ValidationRuleException(final Class<?> clazz,
                                       final String field,
                                       final Object value,
                                       final String message) {
        super(clazz, field, value, message);
    }

    @Override
    public int getCode() {
        return -60005;
    }
}
