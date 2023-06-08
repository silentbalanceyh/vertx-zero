package io.vertx.up.error.elasticsearch;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

/**
 * @author Hongwei
 * @since 2020/1/2, 20:57
 */

public class _404ConfigurationMissingExceptionn extends WebException {
    public _404ConfigurationMissingExceptionn(final Class<?> clazz, final String table) {
        super(clazz, table);
    }

    @Override
    public int getCode() {
        return -20006;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
