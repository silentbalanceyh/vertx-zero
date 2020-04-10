package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

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
