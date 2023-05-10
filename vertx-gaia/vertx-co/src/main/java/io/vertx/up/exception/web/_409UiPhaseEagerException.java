package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;
import io.vertx.up.eon.em.secure.ActPhase;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _409UiPhaseEagerException extends WebException {

    public _409UiPhaseEagerException(final Class<?> clazz, final ActPhase phase) {
        super(clazz, phase.name());
    }

    @Override
    public int getCode() {
        return -80224;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }
}
