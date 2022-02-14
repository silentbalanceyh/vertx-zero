package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

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
