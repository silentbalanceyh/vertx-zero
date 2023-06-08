package io.vertx.mod.rbac.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _401TokenInvalidException extends WebException {

    public _401TokenInvalidException(final Class<?> clazz,
                                     final String token) {
        super(clazz, token);
    }

    @Override
    public int getCode() {
        return -80207;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNAUTHORIZED;
    }
}
