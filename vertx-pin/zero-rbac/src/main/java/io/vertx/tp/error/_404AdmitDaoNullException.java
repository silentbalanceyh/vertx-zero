package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

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
