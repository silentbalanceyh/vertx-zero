package io.vertx.up.exception.booting;

import io.horizon.exception.BootingException;
import io.vertx.core.json.JsonObject;

public class DynamicKeyMissingException extends BootingException {

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
