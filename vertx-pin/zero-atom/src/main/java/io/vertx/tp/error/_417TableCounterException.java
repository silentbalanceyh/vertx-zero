package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _417TableCounterException extends WebException {

    public _417TableCounterException(final Class<?> clazz,
                                     final String size) {
        super(clazz, size);
    }

    @Override
    public int getCode() {
        return -80524;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.EXPECTATION_FAILED;
    }
}
