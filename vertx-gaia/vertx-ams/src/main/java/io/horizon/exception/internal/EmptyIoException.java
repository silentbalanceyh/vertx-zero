package io.horizon.exception.internal;

import io.horizon.annotations.Development;
import io.horizon.eon.error.ErrorCode;
import io.horizon.exception.InternalException;
import io.horizon.util.HUt;

public class EmptyIoException extends InternalException {

    public EmptyIoException(final Class<?> caller, final String filename) {
        super(caller, HUt.fromMessage(ErrorCode._11002.M(), filename));
    }

    @Override
    protected int getCode() {
        return ErrorCode._11002.V();
    }

    @Development("IDE视图专用")
    private int __11002() {
        return this.getCode();
    }
}
