package io.vertx.mod.rbac.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _401CodeGenerationException extends WebException {

    public _401CodeGenerationException(final Class<?> clazz,
                                       final String clientId, final String clientSecret) {
        super(clazz, clientId, clientSecret);
    }

    @Override
    public int getCode() {
        return -80202;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNAUTHORIZED;
    }
}
