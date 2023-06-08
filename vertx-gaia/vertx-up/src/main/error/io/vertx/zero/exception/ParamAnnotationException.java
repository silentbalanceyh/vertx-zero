package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class ParamAnnotationException extends BootingException {

    public ParamAnnotationException(final Class<?> clazz,
                                    final String field,
                                    final int occurs) {
        super(clazz, field, occurs);
    }

    @Override
    public int getCode() {
        return -40030;
    }
}
