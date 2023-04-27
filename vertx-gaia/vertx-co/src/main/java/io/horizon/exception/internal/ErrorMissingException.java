package io.horizon.exception.internal;

import io.horizon.eon.error.ErrorCode;
import io.horizon.exception.InternalException;
import io.horizon.util.HH;

public class ErrorMissingException extends InternalException {

    public ErrorMissingException(final Class<?> caller, final Integer code) {
        super(caller, HH.fromMessage(ErrorCode._11003.M(), String.valueOf(code)));
    }

    @Override
    protected int getCode() {
        return ErrorCode._11003.V();
    }
}
