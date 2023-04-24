package io.vertx.up.exception.heart;

import io.horizon.exception.ZeroRunException;

import java.text.MessageFormat;

public class ErrorMissingException extends ZeroRunException {

    public ErrorMissingException(final Integer code, final String clazz) {
        super(MessageFormat.format(Info.ECODE_MSG, String.valueOf(code), clazz));
    }
}
