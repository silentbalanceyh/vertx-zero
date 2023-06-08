package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _417EventTypeConflictException extends WebException {
    public _417EventTypeConflictException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -80534;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.EXPECTATION_FAILED;
    }
}
