package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _500PoolInternalException extends WebException {

    public _500PoolInternalException(final Class<?> clazz,
                                     final String name,
                                     final String method) {
        super(clazz, name, method);
    }

    @Override
    public int getCode() {
        return -60035;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
