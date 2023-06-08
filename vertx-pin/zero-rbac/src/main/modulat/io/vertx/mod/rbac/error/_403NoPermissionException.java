package io.vertx.mod.rbac.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _403NoPermissionException extends WebException {

    public _403NoPermissionException(final Class<?> clazz,
                                     final String user,
                                     final String profile) {
        super(clazz, user, profile);
    }

    @Override
    public int getCode() {
        return -80212;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.FORBIDDEN;
    }
}
