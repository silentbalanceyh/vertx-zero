package io.vertx.mod.crud.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _409MultiModuleException extends WebException {

    public _409MultiModuleException(final Class<?> clazz,
                                    final int size) {
        super(clazz, String.valueOf(size));
    }

    @Override
    public int getCode() {
        return -80102;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }
}
