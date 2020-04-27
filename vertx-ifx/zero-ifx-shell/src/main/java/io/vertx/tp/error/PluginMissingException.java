package io.vertx.tp.error;

import io.vertx.up.exception.UpException;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class PluginMissingException extends UpException {

    public PluginMissingException(final Class<?> clazz,
                                  final String name) {
        super(clazz, name);
    }

    @Override
    public int getCode() {
        return -40074;
    }
}
