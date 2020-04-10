package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.tp.rbac.atom.ProfileType;
import io.vertx.up.exception.WebException;

public class _403ProfileConflictException extends WebException {

    public _403ProfileConflictException(final Class<?> clazz,
                                        final ProfileType expected,
                                        final ProfileType current) {
        super(clazz, expected.toString(), current.toString());
    }

    @Override
    public int getCode() {
        return -80205;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.FORBIDDEN;
    }
}
