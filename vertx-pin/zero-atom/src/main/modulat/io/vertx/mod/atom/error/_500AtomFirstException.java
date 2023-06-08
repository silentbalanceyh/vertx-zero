package io.vertx.mod.atom.error;

import io.horizon.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _500AtomFirstException extends WebException {

    public _500AtomFirstException(final Class<?> clazz, final String identifier) {
        super(clazz, identifier);
    }

    @Override
    public int getCode() {
        return -80544;
    }
}
