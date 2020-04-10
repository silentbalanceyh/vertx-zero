package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _404ConnectMissingException extends WebException {
    public _404ConnectMissingException(final Class<?> clazz,
                                       final String table) {
        super(clazz, table);
    }

    @Override
    public int getCode() {
        return -60038;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
