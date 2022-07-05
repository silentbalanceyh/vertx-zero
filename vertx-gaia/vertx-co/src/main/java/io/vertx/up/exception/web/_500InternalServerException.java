package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _500InternalServerException extends WebException {

    public _500InternalServerException(final Class<?> clazz,
                                       final String details) {
        super(clazz, clazz, details);
    }

    @Override
    public int getCode() {
        return -60007;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
