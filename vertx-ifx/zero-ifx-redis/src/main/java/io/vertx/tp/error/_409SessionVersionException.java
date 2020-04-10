package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

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
