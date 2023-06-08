package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.IntegrationRequest;

public class _500RequestConfigException extends WebException {

    public _500RequestConfigException(final Class<?> clazz,
                                      final IntegrationRequest request,
                                      final JsonObject data) {
        super(clazz, request.toString(), data.encode());
    }

    @Override
    public int getCode() {
        return -60046;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
