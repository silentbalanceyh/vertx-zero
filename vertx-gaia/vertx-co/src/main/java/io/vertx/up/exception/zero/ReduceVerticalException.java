package io.vertx.up.exception.zero;

import io.vertx.core.json.JsonArray;
import io.vertx.up.exception.UpException;

import java.util.Set;

public class ReduceVerticalException extends UpException {

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
