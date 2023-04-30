package io.vertx.up.exception.web;

import io.horizon.exception.WebException;

public class _400DuplicatedRecordException extends WebException {

    public _400DuplicatedRecordException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -60009;
    }
}
