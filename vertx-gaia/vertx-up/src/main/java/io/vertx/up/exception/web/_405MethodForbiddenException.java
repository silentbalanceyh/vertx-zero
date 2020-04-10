package io.vertx.up.exception.web;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _405MethodForbiddenException extends WebException {

    public _405MethodForbiddenException(final Class<?> clazz,
                                        final HttpMethod method,
                                        final String uri) {
        super(clazz, method, uri);
    }

    @Override
    public int getCode() {
        return -60014;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.METHOD_NOT_ALLOWED;
    }
}
