package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.ext.web.handler.HttpException;
import io.vertx.up.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _000HttpWebException extends WebException {
    private final transient HttpException ex;

    public _000HttpWebException(final Class<?> clazz, final HttpException ex) {
        super(clazz, ex.getStatusCode(), ex.getMessage());
        this.ex = ex;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.fromCode(this.ex.getStatusCode());
    }

    @Override
    public int getCode() {
        return -60049;
    }
}
