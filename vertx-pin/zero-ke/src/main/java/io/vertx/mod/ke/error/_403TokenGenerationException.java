package io.vertx.mod.ke.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _403TokenGenerationException extends WebException {

    public _403TokenGenerationException(final Class<?> clazz, final Integer size) {
        super(clazz, String.valueOf(size));
    }

    @Override
    public int getCode() {
        return -80219;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.FORBIDDEN;
    }
}
