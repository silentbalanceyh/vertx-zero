package io.vertx.aeon.exception.heart;

import io.vertx.up.exception.UpException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ClusterRequiredException extends UpException {

    public ClusterRequiredException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -50002;
    }
}
