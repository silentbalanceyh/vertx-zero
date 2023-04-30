package io.vertx.zero.exception;

import io.horizon.exception.BootingException;
import io.vertx.up.util.Ut;

import java.util.Set;

public class NamedNotFoundException extends BootingException {

    public NamedNotFoundException(final Class<?> clazz,
                                  final Set<String> sets,
                                  final String value) {
        super(clazz, Ut.fromJoin(sets), value);
    }

    @Override
    public int getCode() {
        return -40025;
    }
}
