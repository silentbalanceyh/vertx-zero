package io.vertx.up.exception.demon;

import io.vertx.up.exception.DemonException;

/**
 * Server config:
 * server:
 * -
 */
public class ServerConfigException extends DemonException {
    public ServerConfigException(final Class<?> clazz,
                                 final String config) {
        super(clazz, config);
    }

    @Override
    public int getCode() {
        return -30001;
    }
}
