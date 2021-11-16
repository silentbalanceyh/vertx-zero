package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _501ProcessStartException extends WebException {

    public _501ProcessStartException(final Class<?> clazz, final String definitionId) {
        super(clazz, definitionId);
    }

    @Override
    public int getCode() {
        return -80601;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}