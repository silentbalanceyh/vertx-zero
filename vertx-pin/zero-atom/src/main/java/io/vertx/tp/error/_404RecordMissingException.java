package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _404RecordMissingException extends WebException {

    public _404RecordMissingException(final Class<?> clazz, final String key) {
        super(clazz, key);
    }

    @Override
    public int getCode() {
        return -80529;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
