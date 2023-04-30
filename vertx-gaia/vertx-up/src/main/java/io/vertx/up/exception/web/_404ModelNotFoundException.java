package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _404ModelNotFoundException extends WebException {

    public _404ModelNotFoundException(final Class<?> clazz,
                                      final String namespace,
                                      final String identifier) {
        super(clazz, namespace, identifier);
    }

    @Override
    public int getCode() {
        return -80510;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
