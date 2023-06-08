package io.vertx.mod.workflow.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _404WorkflowNullException extends WebException {

    public _404WorkflowNullException(final Class<?> clazz,
                                     final String definitionKey) {
        super(clazz, definitionKey);
    }

    @Override
    public int getCode() {
        return -80603;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
