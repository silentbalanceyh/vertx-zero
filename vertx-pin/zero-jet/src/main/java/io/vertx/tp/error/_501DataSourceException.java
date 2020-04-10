package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _501DataSourceException extends WebException {

    public _501DataSourceException(final Class<?> clazz,
                                   final String headers) {
        super(clazz, headers);
    }

    @Override
    public int getCode() {
        return -80412;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
