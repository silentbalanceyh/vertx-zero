package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _417DataRowNullException extends WebException {

    public _417DataRowNullException(final Class<?> clazz,
                                    final String identifier) {
        super(clazz, identifier);
    }

    @Override
    public int getCode() {
        return -80533;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.EXPECTATION_FAILED;
    }
}
