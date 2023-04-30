package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _504RemoteRpcException extends WebException {

    public _504RemoteRpcException(final Class<?> clazz,
                                  final String target,
                                  final String address,
                                  final Throwable ex) {
        super(clazz, target, address, ex.getMessage());
    }

    @Override
    public int getCode() {
        return -60017;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.GATEWAY_TIMEOUT;
    }
}
