package io.horizon.exception.web;

import io.horizon.annotations.Development;
import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _500InternalServerException extends WebException {

    public _500InternalServerException(final Class<?> clazz,
                                       final String details) {
        super(clazz, details);
    }

    @Override
    public int getCode() {
        return -60007;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }

    @Development("IDE视图专用")
    private int __60007() {
        return this.getCode();
    }
}
