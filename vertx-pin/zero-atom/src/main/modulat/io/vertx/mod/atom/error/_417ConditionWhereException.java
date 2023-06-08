package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _417ConditionWhereException extends WebException {

    public _417ConditionWhereException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -80523;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.EXPECTATION_FAILED;
    }
}
