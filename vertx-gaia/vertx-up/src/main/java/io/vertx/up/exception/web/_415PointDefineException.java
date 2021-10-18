package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _415PointDefineException extends WebException {
    public _415PointDefineException(final Class<?> clazz,
                                    final Class<?> type) {
        super(clazz, type.getName());
    }

    @Override
    public int getCode() {
        return -60048;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNSUPPORTED_MEDIA_TYPE;
    }
}
