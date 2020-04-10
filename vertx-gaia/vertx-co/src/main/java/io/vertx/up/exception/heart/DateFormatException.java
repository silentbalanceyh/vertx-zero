package io.vertx.up.exception.heart;

import io.vertx.up.exception.ZeroRunException;

import java.text.MessageFormat;

public class DateFormatException extends ZeroRunException {

    public DateFormatException(final String literal) {
        super(MessageFormat.format(Info.DATE_MSG, literal));
    }
}
