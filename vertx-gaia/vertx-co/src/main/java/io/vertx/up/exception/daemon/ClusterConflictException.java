package io.vertx.up.exception.daemon;

import io.horizon.exception.DaemonException;

public class ClusterConflictException extends DaemonException {

    public ClusterConflictException(final Class<?> clazz,
                                    final String name,
                                    final String options) {
        super(clazz, name, options);
    }

    @Override
    public int getCode() {
        return -10004;
    }
}
