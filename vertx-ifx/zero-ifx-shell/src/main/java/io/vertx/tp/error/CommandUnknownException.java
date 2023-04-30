package io.vertx.tp.error;

import io.horizon.exception.BootingException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class CommandUnknownException extends BootingException {

    public CommandUnknownException(final Class<?> clazz, final String command) {
        super(clazz, command);
    }

    @Override
    public int getCode() {
        return -40073;
    }
}
