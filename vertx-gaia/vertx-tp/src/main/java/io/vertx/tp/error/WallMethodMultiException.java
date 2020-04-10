package io.vertx.tp.error;

import io.vertx.up.exception.UpException;

public class WallMethodMultiException extends UpException {

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
