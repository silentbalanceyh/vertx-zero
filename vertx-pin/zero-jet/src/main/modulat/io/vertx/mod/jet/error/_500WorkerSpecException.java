package io.vertx.mod.jet.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _500WorkerSpecException extends WebException {

    public _500WorkerSpecException(final Class<?> clazz,
                                   final Class<?> workerCls) {
        super(clazz, workerCls.getName());
    }

    @Override
    public int getCode() {
        return -80404;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
