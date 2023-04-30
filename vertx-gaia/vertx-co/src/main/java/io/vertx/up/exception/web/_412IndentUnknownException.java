package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _412IndentUnknownException extends WebException {
    public _412IndentUnknownException(final Class<?> clazz, final String targetIndent) {
        super(clazz, targetIndent);
    }

    @Override
    public int getCode() {
        return -80548;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.PRECONDITION_FAILED;
    }
}
