package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

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
