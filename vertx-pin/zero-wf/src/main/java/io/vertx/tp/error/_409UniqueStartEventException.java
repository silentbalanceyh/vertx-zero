package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _409UniqueStartEventException extends WebException {

    public _409UniqueStartEventException(final Class<?> clazz,
                                         final Integer size,
                                         final String definitionId) {
        super(clazz, String.valueOf(size), definitionId);
    }

    @Override
    public int getCode() {
        return -80602;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }
}