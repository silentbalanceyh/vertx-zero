package io.vertx.tp.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _409RepoExistingException extends WebException {
    public _409RepoExistingException(final Class<?> clazz, final String path, final String message) {
        super(clazz, path, message);
    }

    @Override
    public int getCode() {
        return -60055;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }
}
