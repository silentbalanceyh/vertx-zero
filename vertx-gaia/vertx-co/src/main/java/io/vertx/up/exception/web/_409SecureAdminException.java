package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _409SecureAdminException extends WebException {

    public _409SecureAdminException(final Class<?> clazz, final String code) {
        super(clazz, code);
    }

    @Override
    public int getCode() {
        return -80223;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }
}
