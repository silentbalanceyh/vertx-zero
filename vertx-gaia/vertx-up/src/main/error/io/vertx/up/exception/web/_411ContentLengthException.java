package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

/**
 * # 「Error」Zero Exception
 *
 * * Category: WebException
 * * Code: -60052
 * * Status: 411
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _411ContentLengthException extends WebException {

    public _411ContentLengthException(final Class<?> clazz,
                                      final Integer contentLength) {
        super(clazz, contentLength);
    }

    @Override
    public int getCode() {
        return -60052;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.LENGTH_REQUIRED;
    }
}
