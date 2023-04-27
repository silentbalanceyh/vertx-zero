package io.horizon.exception.internal;

import io.horizon.eon.error.ErrorCode;
import io.horizon.exception.InternalException;
import io.horizon.util.HaS;

public class ErrorMissingException extends InternalException {

    public ErrorMissingException(final Class<?> caller, final Integer code) {
        super(caller, HaS.fromMessage(ErrorCode._11003.M(), String.valueOf(code)));
    }

    @Override
    protected int getCode() {
        return ErrorCode._11003.V();
    }
}
