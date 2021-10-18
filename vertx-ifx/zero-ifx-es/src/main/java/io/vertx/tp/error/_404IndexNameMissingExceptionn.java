package io.vertx.tp.error;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/**
 * @author Hongwei
 * @since 2020/1/2, 20:57
 */

public class _404IndexNameMissingExceptionn extends WebException {
    public _404IndexNameMissingExceptionn(final Class<?> clazz, final String table) {
        super(clazz, table);
    }

    @Override
    public int getCode() {
        return -20007;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
