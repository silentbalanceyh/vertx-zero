package io.vertx.mod.workflow.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _404ProcessMissingException extends WebException {

    public _404ProcessMissingException(final Class<?> clazz, final String code) {
        super(clazz, code);
    }

    @Override
    public int getCode() {
        return -80600;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
