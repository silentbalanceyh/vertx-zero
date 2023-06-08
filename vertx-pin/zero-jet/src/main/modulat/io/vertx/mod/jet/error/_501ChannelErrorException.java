package io.vertx.mod.jet.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

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
