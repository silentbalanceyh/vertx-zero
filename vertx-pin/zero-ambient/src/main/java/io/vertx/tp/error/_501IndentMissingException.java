package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.WebException;

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
