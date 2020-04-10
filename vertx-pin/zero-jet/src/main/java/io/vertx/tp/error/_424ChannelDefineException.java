package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _424ChannelDefineException extends WebException {

    public _424ChannelDefineException(final Class<?> clazz,
                                      final String channelName) {
        super(clazz, channelName);
    }

    @Override
    public int getCode() {
        return -80410;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.FAILED_DEPENDENCY;
    }
}
