package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/**
 * Error = 80604
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _409InValidStartException extends WebException {
    public _409InValidStartException(final Class<?> clazz, final String definitionKey) {
        super(clazz, definitionKey);
    }

    @Override
    public int getCode() {
        return -80604;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }
}
