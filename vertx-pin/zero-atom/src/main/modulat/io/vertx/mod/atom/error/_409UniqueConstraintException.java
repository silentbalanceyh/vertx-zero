package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _409UniqueConstraintException extends WebException {

    public _409UniqueConstraintException(final Class<?> clazz,
                                         final Throwable exception) {
        super(clazz, exception.getMessage());
    }

    @Override
    public int getCode() {
        return -80500;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }
}
