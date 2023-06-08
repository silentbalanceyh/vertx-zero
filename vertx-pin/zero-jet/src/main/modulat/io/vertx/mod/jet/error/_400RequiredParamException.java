package io.vertx.mod.jet.error;

import io.horizon.exception.WebException;

public class _400RequiredParamException extends WebException {

    public _400RequiredParamException(final Class<?> clazz, final String fieldname) {
        super(clazz, fieldname);
    }

    @Override
    public int getCode() {
        return -80403;
    }
}
