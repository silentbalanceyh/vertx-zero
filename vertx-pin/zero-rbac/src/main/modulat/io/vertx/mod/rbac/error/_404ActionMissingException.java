package io.vertx.mod.rbac.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _404ActionMissingException extends WebException {

    public _404ActionMissingException(final Class<?> clazz,
                                      final String request) {
        super(clazz, request);
    }

    @Override
    public int getCode() {
        return -80209;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
