package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _401CodeGenerationException extends WebException {

    public _401CodeGenerationException(final Class<?> clazz,
                                       final String clientId, final String clientSecret) {
        super(clazz, clientId, clientSecret);
    }

    @Override
    public int getCode() {
        return -80202;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNAUTHORIZED;
    }
}
