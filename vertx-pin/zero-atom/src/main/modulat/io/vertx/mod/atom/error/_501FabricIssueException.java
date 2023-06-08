package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _501FabricIssueException extends WebException {

    public _501FabricIssueException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -80539;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
