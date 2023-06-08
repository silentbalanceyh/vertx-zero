package io.vertx.mod.jet.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _424ChannelDefinitionException extends WebException {

    public _424ChannelDefinitionException(final Class<?> clazz,
                                          final String expectedChannels,
                                          final Class<?> target) {
        super(clazz, expectedChannels, target.getName(),
            null == target.getSuperclass() ? null : target.getSuperclass().getName());
    }

    @Override
    public int getCode() {
        return -80409;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.FAILED_DEPENDENCY;
    }
}
