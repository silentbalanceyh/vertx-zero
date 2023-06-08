package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _400FileRequiredException extends WebException {

    public _400FileRequiredException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -80526;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.BAD_REQUEST;
    }
}
