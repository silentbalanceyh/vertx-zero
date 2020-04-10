package io.vertx.up.exception.demon;

import io.vertx.up.eon.em.DataType;
import io.vertx.up.exception.DemonException;

public class DataTypeWrongException extends DemonException {

    public DataTypeWrongException(final Class<?> clazz,
                                  final String field,
                                  final Object value,
                                  final DataType type) {
        super(clazz, field, value, type);
    }

    @Override
    public int getCode() {
        return -10003;
    }
}
