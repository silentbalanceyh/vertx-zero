package io.vertx.mod.workflow.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _501EventEndMissingException extends WebException {

    public _501EventEndMissingException(final Class<?> clazz, final String definitionId) {
        super(clazz, definitionId);
    }

    @Override
    public int getCode() {
        return -80608;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
