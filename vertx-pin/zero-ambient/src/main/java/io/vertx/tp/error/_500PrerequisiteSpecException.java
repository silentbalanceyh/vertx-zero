package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _500PrerequisiteSpecException extends WebException {

    public _500PrerequisiteSpecException(final Class<?> clazz,
                                         final String name) {
        super(clazz, name);
    }

    @Override
    public int getCode() {
        return -80303;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
