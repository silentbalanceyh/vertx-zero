package io.vertx.up.exception.boot;

import io.horizon.exception.BootingException;
import io.vertx.up.util.Ut;

import java.util.Set;

public class WallDuplicatedException extends BootingException {

    public WallDuplicatedException(final Class<?> classes,
                                   final Set<String> classNames) {
        super(classes, Ut.fromJoin(classNames));
    }

    @Override
    public int getCode() {
        return -40038;
    }
}
