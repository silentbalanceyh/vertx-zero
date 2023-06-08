package io.vertx.mod.rbac.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _449UserNotFoundException extends WebException {

    public _449UserNotFoundException(final Class<?> clazz,
                                     final String username) {
        super(clazz, username);
    }

    @Override
    public int getCode() {
        return -80203;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.RETRY_WITH;
    }
}
