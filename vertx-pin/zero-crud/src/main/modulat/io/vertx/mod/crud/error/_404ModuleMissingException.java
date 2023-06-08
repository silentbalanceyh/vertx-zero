package io.vertx.mod.crud.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _404ModuleMissingException extends WebException {

    public _404ModuleMissingException(final Class<?> clazz,
                                      final String module) {
        super(clazz, module);
    }

    @Override
    public int getCode() {
        return -80100;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
