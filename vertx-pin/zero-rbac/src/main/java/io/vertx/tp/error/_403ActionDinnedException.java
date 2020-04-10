package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _403ActionDinnedException extends WebException {

    public _403ActionDinnedException(final Class<?> clazz,
                                     final Integer expected,
                                     final Integer actual) {
        super(clazz, expected, actual);
    }

    @Override
    public int getCode() {
        return -80211;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.FORBIDDEN;
    }
}
