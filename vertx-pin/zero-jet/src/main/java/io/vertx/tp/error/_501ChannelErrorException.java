package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _501ChannelErrorException extends WebException {

    public _501ChannelErrorException(final Class<?> clazz,
                                     final String channelName) {
        super(clazz, channelName);
    }

    @Override
    public int getCode() {
        return -80407;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
