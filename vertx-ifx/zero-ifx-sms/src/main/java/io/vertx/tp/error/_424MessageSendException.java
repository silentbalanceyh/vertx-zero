package io.vertx.tp.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _424MessageSendException extends WebException {

    public _424MessageSendException(final Class<?> clazz,
                                    final Throwable ex) {
        super(clazz, ex.getMessage());
    }

    @Override
    public int getCode() {
        return -20004;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.FAILED_DEPENDENCY;
    }
}
