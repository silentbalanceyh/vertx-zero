package io.vertx.up.exception.boot;

import io.horizon.exception.BootingException;

public class DataSourceException extends BootingException {

    public DataSourceException(final Class<?> clazz,
                               final Throwable ex,
                               final String jdbcUrl) {
        super(clazz, jdbcUrl, ex.getMessage());
    }

    @Override
    public int getCode() {
        return -40056;
    }
}

