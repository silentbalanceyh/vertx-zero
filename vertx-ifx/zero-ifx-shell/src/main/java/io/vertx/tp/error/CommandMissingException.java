package io.vertx.tp.error;

import io.horizon.exception.BootingException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class CommandMissingException extends BootingException {

    public CommandMissingException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40072;
    }
}
