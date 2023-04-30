package io.vertx.tp.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _401UnauthorizedException extends WebException {

    public _401UnauthorizedException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -60012;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNAUTHORIZED;
    }
}
