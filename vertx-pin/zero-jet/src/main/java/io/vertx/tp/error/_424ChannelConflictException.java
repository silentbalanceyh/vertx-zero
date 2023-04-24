package io.vertx.tp.error;

import io.horizon.eon.em.container.ChannelType;
import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/*
 * The channel type is not configured correctly
 */
public class _424ChannelConflictException extends WebException {

    public _424ChannelConflictException(final Class<?> clazz,
                                        final String componentName, final ChannelType channelType) {
        super(clazz, componentName, channelType);
    }

    @Override
    public int getCode() {
        return -80408;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.FAILED_DEPENDENCY;
    }
}
