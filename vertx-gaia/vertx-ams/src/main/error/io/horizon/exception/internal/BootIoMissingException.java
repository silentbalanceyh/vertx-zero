package io.horizon.exception.internal;

import io.horizon.annotations.Development;
import io.horizon.eon.error.ErrorCode;
import io.horizon.exception.InternalException;

/**
 * @author lang : 2023-05-30
 */
public class BootIoMissingException extends InternalException {
    public BootIoMissingException(final Class<?> caller) {
        super(caller);
    }

    @Override
    protected int getCode() {
        return ErrorCode._11010.V();
    }

    @Development
    private int __11010() {
        return this.getCode();
    }
}
