package io.vertx.up.exception.heart;

import io.zero.exception.ZeroRunException;

import java.text.MessageFormat;

public class ArgumentException extends ZeroRunException {

    public ArgumentException(
        final Class<?> clazz,
        final String method,
        final Integer length,
        final String op) {
        super(MessageFormat.format(Info.ARG_MSG, method, clazz.getName(), length, op));
    }
}
