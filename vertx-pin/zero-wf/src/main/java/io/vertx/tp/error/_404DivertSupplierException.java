package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _404DivertSupplierException extends WebException {
    public _404DivertSupplierException(final Class<?> clazz, final String eventType) {
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
