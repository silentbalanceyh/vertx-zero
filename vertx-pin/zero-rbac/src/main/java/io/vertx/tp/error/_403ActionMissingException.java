package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _403ActionMissingException extends WebException {

    public _403ActionMissingException(final Class<?> clazz,
                                      final String request) {
        super(clazz, request);
    }

    @Override
    public int getCode() {
        return -80209;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.FORBIDDEN;
    }
}
