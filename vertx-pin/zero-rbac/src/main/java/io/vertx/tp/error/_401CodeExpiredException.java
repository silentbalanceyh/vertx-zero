package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _401CodeExpiredException extends WebException {

    public _401CodeExpiredException(final Class<?> clazz,
                                    final String clientId,
                                    final String code) {
        super(clazz, clientId, code);
    }

    @Override
    public int getCode() {
        return -80201;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNAUTHORIZED;
    }
}
