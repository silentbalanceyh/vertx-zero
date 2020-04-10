package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _417PrimaryAttrWrongException extends WebException {

    public _417PrimaryAttrWrongException(final Class<?> clazz,
                                         final String unique) {
        super(clazz, unique);
    }


    @Override
    public int getCode() {
        return -80535;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.EXPECTATION_FAILED;
    }
}
