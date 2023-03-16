package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

import java.util.Objects;

public class _500InvokeErrorException extends WebException {

    public _500InvokeErrorException(final Class<?> clazz,
                                    final Throwable ex) {
        super(clazz, clazz, Objects.isNull(ex) ? "Null Error" : ex.getMessage());
    }

    @Override
    public int getCode() {
        return -60060;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
