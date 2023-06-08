package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;
import io.vertx.core.json.JsonObject;

public class _500PointerPluginException extends WebException {

    public _500PointerPluginException(final Class<?> clazz,
                                      final JsonObject config) {
        super(clazz, config);
    }

    @Override
    public int getCode() {
        return -60044;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
