package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.WebException;

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
