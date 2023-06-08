package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _417PrimaryKeyMissingException extends WebException {

    public _417PrimaryKeyMissingException(final Class<?> clazz,
                                          final String identifier) {
        super(clazz, identifier);
    }

    @Override
    public int getCode() {
        return -80520;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.EXPECTATION_FAILED;
    }
}
