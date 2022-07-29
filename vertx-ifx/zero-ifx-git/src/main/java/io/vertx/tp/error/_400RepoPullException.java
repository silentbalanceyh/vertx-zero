package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _400RepoPullException extends WebException {

    public _400RepoPullException(final Class<?> clazz,
                                 final String uri,
                                 final String message) {
        super(clazz, uri, message);
    }


    @Override
    public int getCode() {
        return -60057;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.BAD_REQUEST;
    }
}
