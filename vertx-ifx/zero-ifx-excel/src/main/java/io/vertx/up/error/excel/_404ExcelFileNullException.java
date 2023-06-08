package io.vertx.up.error.excel;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _404ExcelFileNullException extends WebException {

    public _404ExcelFileNullException(final Class<?> clazz,
                                      final String filename) {
        super(clazz, filename);
    }

    @Override
    public int getCode() {
        return -60037;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
