package io.vertx.mod.atom.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _417RelationCounterException extends WebException {

    public _417RelationCounterException(final Class<?> clazz,
                                        final String identifier,
                                        final Integer schema,
                                        final Integer joins) {
        super(clazz, identifier, schema, joins);
    }

    @Override
    public int getCode() {
        return -80538;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.EXPECTATION_FAILED;
    }
}
