package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

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
