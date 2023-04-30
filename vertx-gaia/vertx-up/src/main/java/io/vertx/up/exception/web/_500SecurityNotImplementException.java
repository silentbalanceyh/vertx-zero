package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _500SecurityNotImplementException extends WebException {

    public _500SecurityNotImplementException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40061;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
