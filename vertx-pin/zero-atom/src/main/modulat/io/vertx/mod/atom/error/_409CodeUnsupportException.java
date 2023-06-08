package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _409CodeUnsupportException extends WebException {

    public _409CodeUnsupportException(final Class<?> clazz,
                                      final String code) {
        super(clazz, code);
    }

    @Override
    public int getCode() {
        return -80530;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }
}
