package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _409UniqueConstraintException extends WebException {

    public _409UniqueConstraintException(final Class<?> clazz,
                                         final Throwable exception) {
        super(clazz, exception.getMessage());
    }

    @Override
    public int getCode() {
        return -80500;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }
}
