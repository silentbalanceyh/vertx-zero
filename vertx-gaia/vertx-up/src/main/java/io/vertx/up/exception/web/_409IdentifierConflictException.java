package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _409IdentifierConflictException extends WebException {

    public _409IdentifierConflictException(final Class<?> clazz,
                                           final String idInput,
                                           final String idConfig) {
        super(clazz, idInput, idConfig);
    }

    @Override
    public int getCode() {
        return -80547;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }
}
