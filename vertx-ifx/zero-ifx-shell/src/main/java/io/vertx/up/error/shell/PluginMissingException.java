package io.vertx.up.error.shell;

import io.horizon.exception.BootingException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class PluginMissingException extends BootingException {

    public PluginMissingException(final Class<?> clazz,
                                  final String name) {
        super(clazz, name);
    }

    @Override
    public int getCode() {
        return -40074;
    }
}
