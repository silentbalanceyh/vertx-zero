package io.horizon.exception.internal;


import io.horizon.annotations.HDevelop;
import io.horizon.eon.error.ErrorCode;
import io.horizon.exception.InternalException;

public class NotImplementException extends InternalException {

    public NotImplementException(final Class<?> caller) {
        super(caller, ErrorCode._11006.M());
    }

    @Override
    protected int getCode() {
        return ErrorCode._11006.V();
    }

    @HDevelop("IDE视图专用")
    private int __11006() {
        return this.getCode();
    }
}
