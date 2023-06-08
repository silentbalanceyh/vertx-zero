package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;
import io.vertx.core.json.JsonObject;

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
