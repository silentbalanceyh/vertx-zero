package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _424RpcServiceException extends WebException {

    public _424RpcServiceException(final Class<?> clazz,
                                   final String name,
                                   final String address) {
        super(clazz, name, address);
    }

    @Override
    public int getCode() {
        return -60020;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.FAILED_DEPENDENCY;
    }
}
