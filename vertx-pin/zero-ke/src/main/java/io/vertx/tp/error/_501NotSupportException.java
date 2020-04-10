package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _501NotSupportException extends WebException {
    public _501NotSupportException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }

    @Override
    public int getCode() {
        return -80218;
    }
}
