package io.vertx.tp.error;

import io.vertx.up.exception.UpException;

public class WallKeyMissingException extends UpException {

    public WallKeyMissingException(final Class<?> clazz,
                                   final String key,
                                   final Class<?> target) {
        super(clazz, key, target);
    }

    @Override
    public int getCode() {
        return -40040;
    }
}
