package io.vertx.up.exception.booting;

import io.horizon.exception.BootingException;
import io.vertx.core.json.JsonArray;

import java.util.Set;

public class ReduceVerticalException extends BootingException {

    public ReduceVerticalException(final Class<?> clazz,
                                   final JsonArray array,
                                   final String field,
                                   final Set<String> reduced) {
        super(clazz, array.encode(), field, reduced);
    }

    @Override
    public int getCode() {
        return -45001;
    }
}
