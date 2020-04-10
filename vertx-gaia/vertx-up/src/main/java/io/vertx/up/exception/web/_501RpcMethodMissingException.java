package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _501RpcMethodMissingException extends WebException {

    public _501RpcMethodMissingException(final Class<?> clazz,
                                         final String address) {
        super(clazz, address);
    }

    @Override
    public int getCode() {
        return -60018;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
