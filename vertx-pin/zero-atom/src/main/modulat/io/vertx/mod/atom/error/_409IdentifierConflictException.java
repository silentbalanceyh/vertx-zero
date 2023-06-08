package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _409IdentifierConflictException extends WebException {

    public _409IdentifierConflictException(final Class<?> clazz, final String identifier) {
        super(clazz, identifier);
    }

    @Override
    public int getCode() {
        return -80531;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }

}
