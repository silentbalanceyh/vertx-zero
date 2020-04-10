package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _500NullableAddException extends WebException {

    public _500NullableAddException(final Class<?> clazz,
                                    final String table,
                                    final String column) {
        super(clazz, table, column);
    }

    @Override
    public int getCode() {
        return -80504;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
