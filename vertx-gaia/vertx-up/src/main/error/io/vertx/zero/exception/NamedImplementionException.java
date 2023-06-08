package io.vertx.zero.exception;

import io.horizon.exception.BootingException;
import io.vertx.up.util.Ut;

import java.util.Set;

public class NamedImplementionException extends BootingException {

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
