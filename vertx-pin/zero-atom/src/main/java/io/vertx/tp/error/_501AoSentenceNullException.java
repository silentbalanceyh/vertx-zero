package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class _501AoSentenceNullException extends WebException {

    public _501AoSentenceNullException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -80507;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
