package io.vertx.up.exception.demon;

import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.DemonException;

public class ForbiddenFieldException extends DemonException {

    public ForbiddenFieldException(final Class<?> clazz,
                                   final JsonObject data,
                                   final String field) {
        super(clazz, data.encode(), field);
    }

    @Override
    public int getCode() {
        return -10006;
    }
}
