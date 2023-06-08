package io.vertx.up.exception.daemon;

import io.horizon.exception.DaemonException;

/**
 * Server config:
 * server:
 * -
 */
public class ServerConfigException extends DaemonException {
    public ServerConfigException(final Class<?> clazz,
                                 final String config) {
        super(clazz, config);
    }

    @Override
    public int getCode() {
        return -30001;
    }
}
