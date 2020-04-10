package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _500QueryMetaNullException extends WebException {

    public _500QueryMetaNullException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -60024;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
