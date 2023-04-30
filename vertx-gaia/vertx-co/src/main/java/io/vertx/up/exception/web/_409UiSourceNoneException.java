package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _409UiSourceNoneException extends WebException {

    public _409UiSourceNoneException(final Class<?> clazz, final String code) {
        super(clazz, code);
    }

    @Override
    public int getCode() {
        return -80223;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }
}
