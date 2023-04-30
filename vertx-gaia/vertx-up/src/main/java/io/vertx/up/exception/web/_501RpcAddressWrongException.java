package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _501RpcAddressWrongException extends WebException {

    public _501RpcAddressWrongException(final Class<?> clazz,
                                        final String address,
                                        final String name) {
        super(clazz, address, name);
    }

    @Override
    public int getCode() {
        return -60016;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
