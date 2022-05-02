package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _501JooqReferenceException extends WebException {

    public _501JooqReferenceException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -80217;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
