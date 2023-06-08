package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _417TableCounterException extends WebException {

    public _417TableCounterException(final Class<?> clazz,
                                     final String size) {
        super(clazz, size);
    }

    @Override
    public int getCode() {
        return -80524;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.EXPECTATION_FAILED;
    }
}
