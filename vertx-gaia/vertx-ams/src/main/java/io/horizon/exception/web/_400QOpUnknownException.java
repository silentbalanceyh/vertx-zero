package io.horizon.exception.web;

import io.horizon.annotations.Development;
import io.horizon.exception.WebException;

/**
 * 直接从Qr语法中解析的OP不在支持列表范围内。
 *
 * @author lang
 */
public class _400QOpUnknownException extends WebException {

    public _400QOpUnknownException(final Class<?> clazz,
                                   final String op) {
        super(clazz, op);
    }

    @Override
    public int getCode() {
        return -60026;
    }

    @Development("IDE视图专用")
    private int __60026() {
        return this.getCode();
    }
}
