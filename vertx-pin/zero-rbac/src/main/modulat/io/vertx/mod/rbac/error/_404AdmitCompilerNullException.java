package io.vertx.mod.rbac.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;
import io.vertx.up.eon.em.EmSecure;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _404AdmitCompilerNullException extends WebException {
    public _404AdmitCompilerNullException(final Class<?> clazz, final EmSecure.ScIn in) {
        super(clazz, in.name());
    }

    @Override
    public int getCode() {
        return -80225;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
