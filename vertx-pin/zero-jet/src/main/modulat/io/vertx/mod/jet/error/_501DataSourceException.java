package io.vertx.mod.jet.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _501DataSourceException extends WebException {

    public _501DataSourceException(final Class<?> clazz,
                                   final String headers) {
        super(clazz, headers);
    }

    @Override
    public int getCode() {
        return -80412;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
