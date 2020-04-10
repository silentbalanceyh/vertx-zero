package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

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
