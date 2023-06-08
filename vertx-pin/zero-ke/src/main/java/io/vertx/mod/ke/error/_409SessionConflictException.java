package io.vertx.mod.ke.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _409SessionConflictException extends WebException {

    public _409SessionConflictException(final Class<?> clazz,
                                        final String sessionId) {
        super(clazz, sessionId);
    }

    @Override
    public int getCode() {
        return -80214;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }
}
