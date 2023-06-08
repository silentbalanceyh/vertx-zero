package io.vertx.mod.jet.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;
import io.vertx.up.eon.em.EmTraffic;

/*
 * The channel type is not configured correctly
 */
public class _424ChannelConflictException extends WebException {

    public _424ChannelConflictException(final Class<?> clazz,
                                        final String componentName, final EmTraffic.Channel channelType) {
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
