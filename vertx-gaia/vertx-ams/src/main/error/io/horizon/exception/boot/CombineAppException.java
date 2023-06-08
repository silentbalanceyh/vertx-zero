package io.horizon.exception.boot;

import io.horizon.annotations.Development;
import io.horizon.exception.BootingException;

public class CombineAppException extends BootingException {
    public CombineAppException(final Class<?> clazz,
                               final String ns, final String name) {
        super(clazz, ns, name);
    }

    @Override
    public int getCode() {
        return -40101;
    }

    @Development
    private int _40101() {
        return this.getCode();
    }
}
