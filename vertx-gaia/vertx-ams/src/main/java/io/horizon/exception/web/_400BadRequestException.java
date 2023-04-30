package io.horizon.exception.web;

import io.horizon.annotations.Development;
import io.horizon.exception.WebException;

public class _400BadRequestException extends WebException {

    public _400BadRequestException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -60011;
    }

    @Development("IDE视图专用")
    private int __60011() {
        return this.getCode();
    }
}
