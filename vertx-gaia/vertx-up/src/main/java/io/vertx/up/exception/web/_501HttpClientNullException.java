package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _501HttpClientNullException extends WebException {

    public _501HttpClientNullException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -60047;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
