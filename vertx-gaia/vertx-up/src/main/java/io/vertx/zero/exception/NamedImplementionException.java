package io.vertx.zero.exception;

import io.vertx.up.util.Ut;
import io.vertx.up.exception.UpException;

import java.util.Set;

public class NamedImplementionException extends UpException {

    public NamedImplementionException(final Class<?> clazz,
                                      final Set<String> sets,
                                      final String interfaceStr) {
        super(clazz, Ut.fromJoin(sets), interfaceStr);
    }

    @Override
    public int getCode() {
        return -40024;
    }
}
