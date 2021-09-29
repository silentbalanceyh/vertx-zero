package io.vertx.up.secure.proof;

import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.log.Annal;

import java.lang.reflect.Method;

public class AuthDefined {
    private final transient Class<?> clazz;
    private final transient Annal logger;
    private final transient Method[] methods;

    private AuthDefined(final Class<?> clazz) {
        this.clazz = clazz;
        this.logger = Annal.get(clazz);
        this.methods = clazz.getDeclaredMethods();
    }

    public static AuthDefined create(final Class<?> clazz) {
        return new AuthDefined(clazz);
    }

    public AuthDefined verify() {

        return this;
    }

    public void mount(final Aegis reference) {

    }
}
