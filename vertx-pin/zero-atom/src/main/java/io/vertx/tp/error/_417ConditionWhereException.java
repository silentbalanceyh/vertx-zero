package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

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
