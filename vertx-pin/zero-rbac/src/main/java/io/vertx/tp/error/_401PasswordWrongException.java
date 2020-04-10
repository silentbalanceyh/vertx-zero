package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _401PasswordWrongException extends WebException {

    public _401PasswordWrongException(final Class<?> clazz,
                                      final String username) {
        super(clazz, username);
    }

    @Override
    public int getCode() {
        return -80204;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNAUTHORIZED;
    }
}
