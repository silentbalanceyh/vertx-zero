package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _409JoinTargetException extends WebException {

    public _409JoinTargetException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }

    @Override
    public int getCode() {
        return -80542;
    }
}
