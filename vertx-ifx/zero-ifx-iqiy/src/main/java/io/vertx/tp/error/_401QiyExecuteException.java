package io.vertx.tp.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _401QiyExecuteException extends WebException {

    public _401QiyExecuteException(final Class<?> clazz,
                                   final String code,
                                   final String message) {
        super(clazz, code, message);
    }

    @Override
    public int getCode() {
        return -20001;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNAUTHORIZED;
    }
}
