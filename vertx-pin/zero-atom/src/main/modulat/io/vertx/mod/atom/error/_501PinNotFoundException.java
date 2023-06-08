package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _501PinNotFoundException extends WebException {

    public _501PinNotFoundException(final Class<?> clazz, final String key) {
        super(clazz, key);
    }

    @Override
    public int getCode() {
        return -80508;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
