package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _500EmptySQLException extends WebException {

    public _500EmptySQLException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -80502;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
