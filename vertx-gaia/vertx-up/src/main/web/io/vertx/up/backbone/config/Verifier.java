package io.vertx.up.backbone.config;

import io.horizon.uca.log.Annal;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import io.vertx.zero.exception.AccessProxyException;
import io.vertx.zero.exception.NoArgConstructorException;

import java.lang.reflect.Modifier;

class Verifier {

    static void noArg(final Class<?> clazz, final Class<?> target) {
        final Annal logger = Annal.get(target);
        Fn.outBoot(!Ut.isDefaultConstructor(clazz), logger,
            NoArgConstructorException.class,
            logger, clazz);
    }

    static void modifier(final Class<?> clazz, final Class<?> target) {
        final Annal logger = Annal.get(target);
        Fn.outBoot(!Modifier.isPublic(clazz.getModifiers()), logger,
            AccessProxyException.class,
            target, clazz);
    }
}
