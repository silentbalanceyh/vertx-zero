package io.vertx.mod.rbac.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _404ResourceMissingException extends WebException {

    public _404ResourceMissingException(final Class<?> clazz,
                                        final String resourceId,
                                        final String request) {
        super(clazz, resourceId, request);
    }

    @Override
    public int getCode() {
        return -80210;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
