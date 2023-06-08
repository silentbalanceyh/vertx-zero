package io.vertx.mod.rbac.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _404AdmitDaoNullException extends WebException {

    public _404AdmitDaoNullException(final Class<?> clazz,
                                     final String daoStr) {
        super(clazz, daoStr);
    }

    @Override
    public int getCode() {
        return -80226;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
