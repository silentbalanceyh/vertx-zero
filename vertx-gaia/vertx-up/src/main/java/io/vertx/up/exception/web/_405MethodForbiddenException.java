package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;
import io.vertx.core.http.HttpMethod;

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
