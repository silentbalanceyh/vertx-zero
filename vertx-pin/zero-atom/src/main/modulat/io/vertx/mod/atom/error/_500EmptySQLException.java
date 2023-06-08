package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _500EmptySQLException extends WebException {

    public _500EmptySQLException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -80502;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
