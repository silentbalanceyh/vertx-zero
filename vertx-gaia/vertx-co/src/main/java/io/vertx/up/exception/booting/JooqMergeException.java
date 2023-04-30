package io.vertx.up.exception.booting;

import io.horizon.exception.BootingException;

public class JooqMergeException extends BootingException {

    public JooqMergeException(final Class<?> clazz,
                              final Class<?> target,
                              final String data) {
        super(clazz, target, data);
    }

    @Override
    public int getCode() {
        return -40057;
    }
}
