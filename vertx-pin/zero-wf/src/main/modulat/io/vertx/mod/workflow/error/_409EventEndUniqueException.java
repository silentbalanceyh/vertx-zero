package io.vertx.mod.workflow.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _409EventEndUniqueException extends WebException {

    public _409EventEndUniqueException(final Class<?> clazz,
                                       final Integer size,
                                       final String definitionId) {
        super(clazz, String.valueOf(size), definitionId);
    }

    @Override
    public int getCode() {
        return -80609;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }
}