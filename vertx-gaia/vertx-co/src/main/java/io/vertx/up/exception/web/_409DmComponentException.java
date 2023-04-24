package io.vertx.up.exception.web;

import io.aeon.eon.em.ScDim;
import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _409DmComponentException extends WebException {
    public _409DmComponentException(final Class<?> clazz, final ScDim dim) {
        super(clazz, dim.name());
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }

    @Override
    public int getCode() {
        return -60058;
    }
}
