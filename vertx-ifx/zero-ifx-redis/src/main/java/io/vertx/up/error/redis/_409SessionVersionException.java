package io.vertx.up.error.redis;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _409SessionVersionException extends WebException {

    public _409SessionVersionException(final Class<?> clazz,
                                       final int oldVersion,
                                       final int newVersion) {
        super(clazz, String.valueOf(oldVersion), String.valueOf(newVersion));
    }

    @Override
    public int getCode() {
        return -60043;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }
}
