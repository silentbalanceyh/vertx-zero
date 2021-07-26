package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _403PasswordInitException extends WebException {

    public _403PasswordInitException(final Class<?> clazz, final String username) {
        super(clazz, username);
    }

    @Override
    public int getCode() {
        return -80217;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.FORBIDDEN;
    }
}
