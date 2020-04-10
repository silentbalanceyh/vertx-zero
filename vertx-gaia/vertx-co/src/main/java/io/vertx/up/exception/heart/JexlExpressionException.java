package io.vertx.up.exception.heart;

import io.vertx.up.exception.ZeroRunException;

import java.text.MessageFormat;

public class JexlExpressionException extends ZeroRunException {

    public JexlExpressionException(final Class<?> clazz,
                                   final String expression,
                                   final Throwable ex) {
        super(MessageFormat.format(Info.JEXL_MSG, expression, ex.getMessage()));
    }
}
