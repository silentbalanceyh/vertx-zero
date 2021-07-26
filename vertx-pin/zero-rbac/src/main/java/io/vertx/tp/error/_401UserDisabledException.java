package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _401UserDisabledException extends WebException {

    public _401UserDisabledException(final Class<?> clazz, final String username) {
        super(clazz, username);
    }

    @Override
    public int getCode() {
        return -80216;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNAUTHORIZED;
    }
}
