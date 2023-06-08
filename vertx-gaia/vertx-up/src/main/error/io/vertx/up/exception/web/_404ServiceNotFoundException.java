package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;
import io.vertx.core.http.HttpMethod;

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
