package io.vertx.up.uca.rs.config;

import io.vertx.up.log.Annal;
import io.vertx.zero.exception.AccessProxyException;
import io.vertx.zero.exception.NoArgConstructorException;
import io.vertx.up.util.Ut;
import io.vertx.up.fn.Fn;

import java.lang.reflect.Modifier;

class Verifier {

    static void noArg(final Class<?> clazz, final Class<?> target) {
        final Annal logger = Annal.get(target);
        Fn.outUp(!Ut.withNoArgConstructor(clazz), logger,
                NoArgConstructorException.class,
                logger, clazz);
    }

    static void modifier(final Class<?> clazz, final Class<?> target) {
        final Annal logger = Annal.get(target);
        Fn.outUp(!Modifier.isPublic(clazz.getModifiers()), logger,
                AccessProxyException.class,
                target, clazz);
    }
}
