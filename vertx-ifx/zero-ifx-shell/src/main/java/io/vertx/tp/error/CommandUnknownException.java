package io.vertx.tp.error;

import io.vertx.up.exception.UpException;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class CommandUnknownException extends UpException {

    public CommandUnknownException(final Class<?> clazz, final String command) {
        super(clazz, command);
    }

    @Override
    public int getCode() {
        return -40073;
    }
}
