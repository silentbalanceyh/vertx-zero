package io.vertx.up.secure.error;

import io.vertx.up.eon.em.AuthWall;
import io.vertx.up.exception.UpException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ProviderMissingException extends UpException {
    public ProviderMissingException(final Class<?> clazz,
                                    final AuthWall wall) {
        super(clazz, wall.key());
    }

    @Override
    public int getCode() {
        return 40078;
    }
}
