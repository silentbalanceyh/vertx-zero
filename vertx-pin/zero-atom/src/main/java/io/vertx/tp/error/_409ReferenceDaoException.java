package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _409ReferenceDaoException extends WebException {

    public _409ReferenceDaoException(final Class<?> clazz, final String source, final String attributeName) {
        super(clazz, source, attributeName);
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }

    @Override
    public int getCode() {
        return -80541;
    }
}
