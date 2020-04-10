package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _400TrackingErrorException extends WebException {
    public _400TrackingErrorException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -300008;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.BAD_REQUEST;
    }
}
