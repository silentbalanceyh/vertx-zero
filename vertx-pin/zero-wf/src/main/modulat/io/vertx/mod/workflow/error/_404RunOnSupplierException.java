package io.vertx.mod.workflow.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

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
