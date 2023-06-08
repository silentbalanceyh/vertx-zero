package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;
import io.vertx.core.json.JsonObject;

public class _401JwtExpiredException extends WebException {

    public _401JwtExpiredException(final Class<?> clazz,
                                   final JsonObject payload) {
        super(clazz, payload);
    }

    @Override
    public int getCode() {
        return -60029;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNAUTHORIZED;
    }
}
