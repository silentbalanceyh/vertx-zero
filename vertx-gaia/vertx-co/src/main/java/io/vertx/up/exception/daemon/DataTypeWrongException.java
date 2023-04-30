package io.vertx.up.exception.daemon;

import io.horizon.eon.em.typed.JsonType;
import io.horizon.exception.DaemonException;

public class DataTypeWrongException extends DaemonException {

    public DataTypeWrongException(final Class<?> clazz,
                                  final String field,
                                  final Object value,
                                  final JsonType type) {
        super(clazz, field, value, type);
    }

    @Override
    public int getCode() {
        return -10003;
    }
}
