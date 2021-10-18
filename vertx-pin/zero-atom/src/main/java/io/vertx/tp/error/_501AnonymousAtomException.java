package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _501AnonymousAtomException extends WebException {

    public _501AnonymousAtomException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -80540;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
