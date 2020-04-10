package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _500DefinitionErrorException extends WebException {
    public _500DefinitionErrorException(final Class<?> clazz, final String key) {
        super(clazz, key);
    }

    @Override
    public int getCode() {
        return -80406;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
