package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/**
 * # 「Error」Zero Exception
 *
 * * Category: WebException
 * * Code: -40068
 * * Status: 411
 *
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class _411ContentLengthException extends WebException {

    public _411ContentLengthException(final Class<?> clazz,
                                      final Integer contentLength) {
        super(clazz, contentLength);
    }

    @Override
    public int getCode() {
        return -40068;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.LENGTH_REQUIRED;
    }
}
