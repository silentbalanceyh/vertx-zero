package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _409SessionConflictException extends WebException {

    public _409SessionConflictException(final Class<?> clazz,
                                        final String sessionId) {
        super(clazz, sessionId);
    }

    @Override
    public int getCode() {
        return -80214;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }
}
