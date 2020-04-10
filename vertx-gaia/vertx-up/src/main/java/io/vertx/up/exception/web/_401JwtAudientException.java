package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _401JwtAudientException extends WebException {

    public _401JwtAudientException(final Class<?> clazz,
                                   final String audients) {
        super(clazz, audients);
    }

    @Override
    public int getCode() {
        return -60030;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNAUTHORIZED;
    }
}
