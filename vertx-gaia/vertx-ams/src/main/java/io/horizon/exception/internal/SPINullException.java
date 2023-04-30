package io.horizon.exception.internal;

import io.horizon.annotations.HDevelop;
import io.horizon.eon.error.ErrorCode;
import io.horizon.exception.InternalException;

/**
 * @author lang : 2023/4/27
 */
public class SPINullException extends InternalException {

    public SPINullException(final Class<?> clazz) {
        super(clazz, ErrorCode._11000.M());
    }

    @Override
    protected int getCode() {
        return ErrorCode._11000.V();
    }

    @HDevelop("IDE视图专用")
    private int __11000() {
        return this.getCode();
    }
}
