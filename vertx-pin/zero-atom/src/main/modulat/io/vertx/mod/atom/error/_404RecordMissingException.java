package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _404RecordMissingException extends WebException {

    public _404RecordMissingException(final Class<?> clazz, final String key) {
        super(clazz, key);
    }

    @Override
    public int getCode() {
        return -80529;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
