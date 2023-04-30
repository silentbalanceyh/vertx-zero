package io.vertx.tp.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _404ConnectMissingException extends WebException {
    public _404ConnectMissingException(final Class<?> clazz,
                                       final String table) {
        super(clazz, table);
    }

    @Override
    public int getCode() {
        return -60038;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
