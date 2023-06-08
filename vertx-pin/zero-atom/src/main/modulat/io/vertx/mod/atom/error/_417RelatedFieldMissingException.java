package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _417RelatedFieldMissingException extends WebException {

    public _417RelatedFieldMissingException(final Class<?> clazz,
                                            final String entityKey,
                                            final String entity,
                                            final String unique) {
        super(clazz, entityKey, entity, unique);
    }

    @Override
    public int getCode() {
        return -80536;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.EXPECTATION_FAILED;
    }
}
