package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _417PrimaryKeyResultException extends WebException {

    public _417PrimaryKeyResultException(final Class<?> clazz,
                                         final String name,
                                         final String key) {
        super(clazz, name, key);
    }

    @Override
    public int getCode() {
        return -80514;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.EXPECTATION_FAILED;
    }
}
