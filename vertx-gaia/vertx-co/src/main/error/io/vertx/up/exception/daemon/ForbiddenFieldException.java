package io.vertx.up.exception.daemon;

import io.horizon.exception.DaemonException;
import io.vertx.core.json.JsonObject;

public class ForbiddenFieldException extends DaemonException {

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
