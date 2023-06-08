package io.vertx.mod.ambient.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;
import io.vertx.core.json.JsonObject;

public class _501IndentMissingException extends WebException {

    public _501IndentMissingException(final Class<?> clazz, final JsonObject config) {
        super(clazz, config.encode());
    }

    @Override
    public int getCode() {
        return -80305;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
