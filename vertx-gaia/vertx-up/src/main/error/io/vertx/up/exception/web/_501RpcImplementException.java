package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

import java.lang.reflect.Method;

public class _501RpcImplementException extends WebException {

    public _501RpcImplementException(final Class<?> clazz,
                                     final String name,
                                     final String address,
                                     final Method method) {
        super(clazz, name, address, method.getName(), method.getDeclaringClass());
    }

    @Override
    public int getCode() {
        return -60015;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
