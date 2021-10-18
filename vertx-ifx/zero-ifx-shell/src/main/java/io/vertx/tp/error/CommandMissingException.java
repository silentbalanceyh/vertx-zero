package io.vertx.tp.error;

import io.vertx.up.exception.UpException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class CommandMissingException extends UpException {

    public CommandMissingException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40072;
    }
}
