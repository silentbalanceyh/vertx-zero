package io.vertx.aeon.exception.heart;

import io.vertx.up.exception.UpException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AeonConfigureException extends UpException {

    public AeonConfigureException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -50001;
    }
}
