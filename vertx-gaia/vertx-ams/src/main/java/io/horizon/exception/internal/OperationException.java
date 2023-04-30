package io.horizon.exception.internal;

import io.horizon.annotations.HDevelop;
import io.horizon.eon.error.ErrorCode;
import io.horizon.exception.InternalException;
import io.horizon.util.HaS;

public class OperationException extends InternalException {
    public OperationException(final Class<?> caller, final String method) {
        super(caller, HaS.fromMessage(ErrorCode._11005.M(), method, caller.getName()));
    }

    public OperationException(final Class<?> caller, final String method, final Class<?> clazz) {
        super(caller, HaS.fromMessage(ErrorCode._11005.M(), method, clazz.getName()));
    }

    @Override
    protected int getCode() {
        return ErrorCode._11005.V();
    }

    @HDevelop("IDE视图专用")
    private int __11005() {
        return this.getCode();
    }
}
