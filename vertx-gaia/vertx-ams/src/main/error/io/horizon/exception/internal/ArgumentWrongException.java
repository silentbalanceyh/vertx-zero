package io.horizon.exception.internal;

import io.horizon.annotations.Development;
import io.horizon.eon.error.ErrorCode;
import io.horizon.exception.InternalException;

/**
 * @author lang : 2023/4/27
 */
public class ArgumentWrongException extends InternalException {

    public ArgumentWrongException(final Class<?> caller, final String message) {
        super(caller, message);
    }

    @Override
    protected int getCode() {
        return ErrorCode._11001.V();
    }

    @Development("IDE视图专用")
    private int __11001() {
        return this.getCode();
    }
}
