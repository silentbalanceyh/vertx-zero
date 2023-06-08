package io.vertx.mod.rbac.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _423UserDisabledException extends WebException {

    public _423UserDisabledException(final Class<?> clazz, final String username) {
        super(clazz, username);
    }

    @Override
    public int getCode() {
        return -80220;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.LOCKED;
    }
}
