package io.vertx.tp.error;

import io.horizon.exception.BootingException;

public class WallMethodMultiException extends BootingException {

    public WallMethodMultiException(final Class<?> clazz,
                                    final String annoCls,
                                    final String targetCls) {
        super(clazz, annoCls, targetCls);
    }

    @Override
    public int getCode() {
        return -40041;
    }
}
