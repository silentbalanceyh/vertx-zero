package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _404RunOnSupplierException extends WebException {
    public _404RunOnSupplierException(final Class<?> clazz, final String eventType) {
        super(clazz, eventType);
    }

    @Override
    public int getCode() {
        return -80607;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
