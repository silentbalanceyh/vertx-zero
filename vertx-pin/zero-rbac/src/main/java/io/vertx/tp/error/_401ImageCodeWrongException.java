package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _401ImageCodeWrongException extends WebException {

    public _401ImageCodeWrongException(final Class<?> clazz,
                                       final String code) {
        super(clazz, code);
    }

    @Override
    public int getCode() {
        return -80222;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNAUTHORIZED;
    }
}
