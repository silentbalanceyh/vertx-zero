package io.vertx.mod.rbac.error;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _401MaximumTimesException extends WebException {
    public _401MaximumTimesException(final Class<?> clazz,
                                     final Integer times,
                                     final Integer seconds) {
        super(clazz, String.valueOf(times), String.valueOf(Objects.requireNonNull(seconds) / 60));
    }

    @Override
    public int getCode() {
        return -80221;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNAUTHORIZED;
    }
}
