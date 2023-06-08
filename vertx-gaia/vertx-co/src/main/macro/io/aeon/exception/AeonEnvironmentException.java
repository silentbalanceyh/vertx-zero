package io.aeon.exception;

import io.horizon.exception.BootingException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AeonEnvironmentException extends BootingException {

    public AeonEnvironmentException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -50003;
    }
}
