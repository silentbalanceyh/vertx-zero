package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _409JobFormulaErrorException extends WebException {

    public _409JobFormulaErrorException(final Class<?> clazz,
                                        final String formula) {
        super(clazz, formula);
    }

    @Override
    public int getCode() {
        return -60054;
    }


    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.CONFLICT;
    }
}
