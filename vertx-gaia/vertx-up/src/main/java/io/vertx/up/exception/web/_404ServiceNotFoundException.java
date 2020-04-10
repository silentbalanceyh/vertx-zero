package io.vertx.up.exception.web;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _404ServiceNotFoundException extends WebException {

    public _404ServiceNotFoundException(final Class<?> clazz,
                                        final String uri,
                                        final HttpMethod method) {
        super(clazz, uri, method);
    }

    @Override
    public int getCode() {
        return -60010;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
