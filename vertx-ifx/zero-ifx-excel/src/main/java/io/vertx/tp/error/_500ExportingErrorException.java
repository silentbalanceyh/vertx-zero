package io.vertx.tp.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _500ExportingErrorException extends WebException {

    public _500ExportingErrorException(final Class<?> clazz,
                                       final String detail) {
        super(clazz, detail);
    }

    @Override
    public int getCode() {
        return -60039;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
