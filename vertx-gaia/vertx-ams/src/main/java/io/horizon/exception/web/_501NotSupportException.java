package io.horizon.exception.web;

import io.horizon.annotations.Development;
import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _501NotSupportException extends WebException {
    public _501NotSupportException(final Class<?> clazz) {
        super(clazz, clazz);
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }

    @Override
    public int getCode() {
        return -60050;
    }

    @Development("IDE视图专用")
    private int __60050() {
        return this.getCode();
    }
}
