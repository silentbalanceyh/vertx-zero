package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _417JobMethodException extends WebException {
    public _417JobMethodException(final Class<?> clazz, final String name) {
        super(clazz, name);
    }

    @Override
    public int getCode() {
        return -60041;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.EXPECTATION_FAILED;
    }
}
