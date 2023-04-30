package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _400SigmaMissingException extends WebException {

    public _400SigmaMissingException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -60045;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.BAD_REQUEST;
    }
}
