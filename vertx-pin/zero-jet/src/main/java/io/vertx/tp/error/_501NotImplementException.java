package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _501NotImplementException extends WebException {

    public _501NotImplementException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -80413;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
