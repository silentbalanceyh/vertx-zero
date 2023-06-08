package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _500TypeAlterException extends WebException {

    public _500TypeAlterException(final Class<?> clazz,
                                  final String table,
                                  final String column) {
        super(clazz, table, column);
    }

    @Override
    public int getCode() {
        return -80509;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
