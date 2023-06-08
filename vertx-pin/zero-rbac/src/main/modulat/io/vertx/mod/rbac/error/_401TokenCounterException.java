package io.vertx.mod.rbac.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _401TokenCounterException extends WebException {
    public _401TokenCounterException(final Class<?> clazz,
                                     final Integer size,
                                     final String userKey) {
        super(clazz, size, userKey);
    }

    @Override
    public int getCode() {
        return -80206;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNAUTHORIZED;
    }
}
