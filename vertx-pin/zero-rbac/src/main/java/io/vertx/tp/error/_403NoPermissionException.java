package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

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
