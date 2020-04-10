package io.vertx.up.exception.demon;

import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.DemonException;

public class RequiredFieldException extends DemonException {

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
