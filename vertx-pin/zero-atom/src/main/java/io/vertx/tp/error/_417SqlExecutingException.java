package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.WebException;

public class _417SqlExecutingException extends WebException {

    public _417SqlExecutingException(final Class<?> clazz,
                                     final String sql,
                                     final JsonObject data) {
        super(clazz, sql, data.encode());
    }

    @Override
    public int getCode() {
        return -80521;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.EXPECTATION_FAILED;
    }
}
