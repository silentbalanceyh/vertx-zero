package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _501ChannelNotImplementException extends WebException {

    public _501ChannelNotImplementException(final Class<?> clazz,
                                            final Class<?> interfaceCls) {
        super(clazz, interfaceCls.getName());
    }

    @Override
    public int getCode() {
        return -80216;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
