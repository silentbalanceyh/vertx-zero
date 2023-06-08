package io.vertx.zero.exception;

import io.horizon.exception.BootingException;
import io.vertx.up.util.Ut;

import java.util.Set;

public class MultiAnnotatedException extends BootingException {

    public MultiAnnotatedException(final Class<?> clazz,
                                   final String className,
                                   final String name,
                                   final Set<String> set) {
        super(clazz, className, name, Ut.fromJoin(set));
    }

    @Override
    public int getCode() {
        return -40021;
    }
}
