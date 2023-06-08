package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _417ConditionEmptyException extends WebException {

    public _417ConditionEmptyException(final Class<?> clazz,
                                       final String table) {
        super(clazz, table);
    }

    @Override
    public int getCode() {
        return -80522;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.EXPECTATION_FAILED;
    }
}
