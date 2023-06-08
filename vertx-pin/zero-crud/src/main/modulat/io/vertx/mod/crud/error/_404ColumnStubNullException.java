package io.vertx.mod.crud.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _404ColumnStubNullException extends WebException {

    public _404ColumnStubNullException(final Class<?> clazz,
                                       final String name) {
        super(clazz, name);
    }

    @Override
    public int getCode() {
        return -80101;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
