package io.vertx.up.exception.booting;

import io.horizon.exception.BootingException;

public class InjectionLimeKeyException extends BootingException {

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
