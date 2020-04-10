package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _401TokenExpiredException extends WebException {

    public _401TokenExpiredException(final Class<?> clazz,
                                     final String token) {
        super(clazz, token);
    }

    @Override
    public int getCode() {
        return -80208;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.RETRY_WITH;
    }
}
