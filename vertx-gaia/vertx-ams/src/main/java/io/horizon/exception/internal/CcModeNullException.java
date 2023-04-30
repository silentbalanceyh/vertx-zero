package io.horizon.exception.internal;

import io.horizon.annotations.HDevelop;
import io.horizon.eon.error.ErrorCode;
import io.horizon.exception.InternalException;

/**
 * @author lang : 2023/4/27
 */
public class CcModeNullException extends InternalException {
    public CcModeNullException(final Class<?> caller) {
        super(caller, ErrorCode._11009.M());
    }

    @Override
    protected int getCode() {
        return ErrorCode._11009.V();
    }

    @HDevelop("IDE视图专用")
    private int __11009() {
        return this.getCode();
    }
}
