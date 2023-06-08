package io.vertx.mod.rbac.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _401CodeWrongException extends WebException {

    public _401CodeWrongException(final Class<?> clazz,
                                  final String code) {
        super(clazz, code);
    }

    @Override
    public int getCode() {
        return -80200;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNAUTHORIZED;
    }
}
