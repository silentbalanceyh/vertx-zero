package io.vertx.tp.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _401QiyTokenException extends WebException {

    public _401QiyTokenException(final Class<?> clazz,
                                 final String clientId) {
        super(clazz, clientId);
    }

    @Override
    public int getCode() {
        return -20002;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNAUTHORIZED;
    }
}
