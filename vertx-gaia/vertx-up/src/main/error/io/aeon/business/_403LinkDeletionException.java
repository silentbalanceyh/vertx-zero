package io.aeon.business;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

/**
 * @author lang : 2023-05-27
 */
public class _403LinkDeletionException extends WebException {

    public _403LinkDeletionException(final Class<?> clazz,
                                     final String identifier) {
        super(clazz, identifier);
    }

    @Override
    public int getCode() {
        return -80306;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.FORBIDDEN;
    }
}
