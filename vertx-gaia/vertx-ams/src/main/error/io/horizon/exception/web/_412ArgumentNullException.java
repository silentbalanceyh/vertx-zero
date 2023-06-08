package io.horizon.exception.web;

import io.horizon.annotations.Development;
import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _412ArgumentNullException extends WebException {

    public _412ArgumentNullException(final Class<?> clazz,
                                     final String message) {
        super(clazz, message);
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.PRECONDITION_FAILED;
    }

    @Override
    public int getCode() {
        return -60059;
    }

    @Development("IDE视图专用")
    private int __60059() {
        return this.getCode();
    }
}
