package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _500TypeAlterException extends WebException {

    public _500TypeAlterException(final Class<?> clazz,
                                  final String table,
                                  final String column) {
        super(clazz, table, column);
    }

    @Override
    public int getCode() {
        return -80509;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
