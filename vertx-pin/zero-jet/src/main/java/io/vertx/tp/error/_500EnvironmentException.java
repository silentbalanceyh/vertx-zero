package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _500EnvironmentException extends WebException {
    _500EnvironmentException(final Class<?> clazz, final String sigma) {
        super(clazz, sigma);
    }

    @Override
    public int getCode() {
        return -80411;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
