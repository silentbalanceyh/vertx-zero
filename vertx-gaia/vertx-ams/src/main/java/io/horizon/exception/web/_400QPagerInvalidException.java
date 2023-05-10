package io.horizon.exception.web;

import io.horizon.annotations.Development;
import io.horizon.exception.WebException;

public class _400QPagerInvalidException extends WebException {

    public _400QPagerInvalidException(final Class<?> clazz,
                                      final String key) {
        super(clazz, key);
    }

    @Override
    public int getCode() {
        return -60023;
    }

    @Development("IDE视图专用")
    private int __60023() {
        return this.getCode();
    }
}
