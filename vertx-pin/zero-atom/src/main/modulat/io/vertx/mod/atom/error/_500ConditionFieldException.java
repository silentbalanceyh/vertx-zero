package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _500ConditionFieldException extends WebException {

    public _500ConditionFieldException(final Class<?> clazz,
                                       final String field) {
        super(clazz, field);
    }

    @Override
    public int getCode() {
        return -80525;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
