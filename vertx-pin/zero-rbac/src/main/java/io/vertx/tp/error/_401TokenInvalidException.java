package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _401TokenInvalidException extends WebException {

    public _401TokenInvalidException(final Class<?> clazz,
                                     final String token) {
        super(clazz, token);
    }

    @Override
    public int getCode() {
        return -80207;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNAUTHORIZED;
    }
}
