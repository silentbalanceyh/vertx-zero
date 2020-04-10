package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

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
