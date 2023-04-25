package io.aeon.exception.heart;

import io.vertx.up.exception.UpException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AeonEnvironmentException extends UpException {

    public AeonEnvironmentException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -50003;
    }
}
