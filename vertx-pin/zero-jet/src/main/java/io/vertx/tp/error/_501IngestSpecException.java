package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _501IngestSpecException extends WebException {
    public _501IngestSpecException(final Class<?> clazz, final String name) {
        super(clazz, name);
    }

    @Override
    public int getCode() {
        return -80402;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
