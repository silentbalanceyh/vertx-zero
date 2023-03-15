package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

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
