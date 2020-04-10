package io.vertx.up.exception.zero;

import io.vertx.up.exception.UpException;

public class InjectionLimeKeyException extends UpException {

    public InjectionLimeKeyException(final Class<?> clazz,
                                     final Class<?> infixCls,
                                     final String key) {
        super(clazz, infixCls.getName(), key);
    }

    @Override
    public int getCode() {
        return -40026;
    }
}
