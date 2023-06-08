package io.horizon.exception.boot;

import io.horizon.annotations.Development;
import io.horizon.exception.BootingException;

public class AmbientConnectException extends BootingException {

    public AmbientConnectException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40103;
    }

    @Development
    private int _40103() {
        return this.getCode();
    }
}
