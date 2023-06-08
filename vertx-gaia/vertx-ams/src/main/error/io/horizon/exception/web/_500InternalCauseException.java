package io.horizon.exception.web;

import io.horizon.annotations.Development;
import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

import java.util.Objects;

public class _500InternalCauseException extends WebException {

    public _500InternalCauseException(final Class<?> clazz,
                                      final Throwable ex) {
        super(clazz, clazz, Objects.isNull(ex) ? "Null Error" : ex.getMessage());
    }

    @Override
    public int getCode() {
        return -60060;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }

    @Development("IDE视图专用")
    private int __60060() {
        return this.getCode();
    }
}
