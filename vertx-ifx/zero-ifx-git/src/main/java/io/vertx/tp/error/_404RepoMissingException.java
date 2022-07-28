package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

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
