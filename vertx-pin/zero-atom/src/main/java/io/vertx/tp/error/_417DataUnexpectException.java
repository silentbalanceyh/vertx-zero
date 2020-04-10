package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _417DataUnexpectException extends WebException {

    public _417DataUnexpectException(final Class<?> clazz,
                                     final String table,
                                     final String rows) {
        super(clazz, table, rows);
    }

    @Override
    public int getCode() {
        return -80519;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.EXPECTATION_FAILED;
    }
}
