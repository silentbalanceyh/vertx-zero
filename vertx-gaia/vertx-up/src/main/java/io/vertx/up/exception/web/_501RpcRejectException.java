package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _501RpcRejectException extends WebException {

    public _501RpcRejectException(final Class<?> clazz) {
        super(clazz, clazz);
    }

    @Override
    public int getCode() {
        return -60027;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
