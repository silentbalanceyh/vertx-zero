package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

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
