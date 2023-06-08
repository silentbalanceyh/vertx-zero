package io.vertx.up.exception.daemon;

import io.horizon.exception.DaemonException;
import io.vertx.core.json.JsonObject;

public class RequiredFieldException extends DaemonException {

    public RequiredFieldException(final Class<?> clazz,
                                  final JsonObject data,
                                  final String field) {
        super(clazz, data.encode(), field);
    }

    @Override
    public int getCode() {
        return -10002;
    }
}
