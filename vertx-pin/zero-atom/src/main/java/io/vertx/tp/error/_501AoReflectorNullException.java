package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _501AoReflectorNullException extends WebException {

    public _501AoReflectorNullException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -80506;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
