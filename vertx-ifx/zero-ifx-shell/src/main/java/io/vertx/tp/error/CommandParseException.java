package io.vertx.tp.error;

import io.vertx.up.exception.UpException;
import org.apache.commons.cli.ParseException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class CommandParseException extends UpException {

    public CommandParseException(final Class<?> clazz, final String input,
                                 final ParseException error) {
        super(clazz, input, error.getMessage());
    }

    @Override
    public int getCode() {
        return -40071;
    }
}
