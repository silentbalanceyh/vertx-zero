package io.vertx.mod.rbac.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _500DwarfInstanceNullException extends WebException {

    public _500DwarfInstanceNullException(final Class<?> clazz,
                                          final String type) {
        super(clazz, type);
    }

    @Override
    public int getCode() {
        return -80215;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
