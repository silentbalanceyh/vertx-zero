package io.vertx.up.exception.zero;

import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.UpException;

public class DynamicKeyMissingException extends UpException {

    public DynamicKeyMissingException(final Class<?> clazz,
                                      final String key,
                                      final JsonObject data) {
        super(clazz, key, data);
    }

    @Override
    public int getCode() {
        return -10005;
    }
}
