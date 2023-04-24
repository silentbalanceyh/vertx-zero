package io.vertx.tp.error;

import io.aeon.eon.em.ScIn;
import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _404AdmitCompilerNullException extends WebException {
    public _404AdmitCompilerNullException(final Class<?> clazz, final ScIn in) {
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
