package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _412NullValueException extends WebException {

    public _412NullValueException(final Class<?> clazz,
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
}
