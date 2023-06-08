package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

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
