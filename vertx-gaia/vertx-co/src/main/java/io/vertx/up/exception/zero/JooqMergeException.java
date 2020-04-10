package io.vertx.up.exception.zero;

import io.vertx.up.exception.UpException;

public class JooqMergeException extends UpException {

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
