package io.aeon.exception;

import io.horizon.exception.BootingException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AeonConfigureException extends BootingException {

    public AeonConfigureException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -50001;
    }
}
