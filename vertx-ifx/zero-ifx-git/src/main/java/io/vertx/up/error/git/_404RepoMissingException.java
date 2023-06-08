package io.vertx.up.error.git;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _404RepoMissingException extends WebException {
    public _404RepoMissingException(final Class<?> clazz, final String path, final String message) {
        super(clazz, path, message);
    }

    @Override
    public int getCode() {
        return -60056;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
