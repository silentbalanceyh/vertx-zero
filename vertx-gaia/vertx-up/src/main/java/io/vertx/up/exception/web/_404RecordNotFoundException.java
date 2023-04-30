package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _404RecordNotFoundException extends WebException {

    public _404RecordNotFoundException(final Class<?> clazz) {
        super(clazz);
        status = HttpStatusCode.NOT_FOUND;
    }

    @Override
    public int getCode() {
        return -60008;
    }

    @Override
    public HttpStatusCode getStatus() {
        return status;
    }
}
