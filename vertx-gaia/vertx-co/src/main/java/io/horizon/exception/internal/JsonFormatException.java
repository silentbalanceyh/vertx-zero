package io.horizon.exception.internal;

import io.horizon.eon.error.ErrorCode;
import io.horizon.exception.InternalException;
import io.horizon.util.HH;

public class JsonFormatException extends InternalException {
    public JsonFormatException(final Class<?> caller, final String filename) {
        super(caller, HH.fromMessage(ErrorCode._11004.M(), filename));
    }

    @Override
    protected int getCode() {
        return ErrorCode._11004.V();
    }
}
