package io.horizon.exception.web;

import io.horizon.annotations.HDevelop;
import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _401UnauthorizedException extends WebException {

    public _401UnauthorizedException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -60012;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNAUTHORIZED;
    }

    @HDevelop("IDE视图专用")
    private int __60012() {
        return this.getCode();
    }
}
