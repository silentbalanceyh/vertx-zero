package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _500NullableAlterException extends WebException {

    public _500NullableAlterException(final Class<?> clazz,
                                      final String table,
                                      final String column) {
        super(clazz, table, column);
    }

    @Override
    public int getCode() {
        return -80505;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
