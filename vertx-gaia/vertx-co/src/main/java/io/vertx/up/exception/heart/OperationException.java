package io.vertx.up.exception.heart;

import io.horizon.exception.ZeroRunException;

import java.text.MessageFormat;

public class OperationException extends ZeroRunException {

    public OperationException(final String method, final Class<?> clazz) {
        super(MessageFormat.format(Info.OP_MSG, method, clazz));
    }
}
