package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

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
