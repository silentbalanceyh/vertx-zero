package io.vertx.up.secure.inquire;

import io.vertx.up.atom.secure.Cliff;
import io.vertx.up.log.Annal;

import java.lang.reflect.Method;

public class OstiumAuth {
    private final transient Class<?> clazz;
    private final transient Annal logger;
    private final transient Method[] methods;

    public static OstiumAuth create(final Class<?> clazz) {
        return new OstiumAuth(clazz);
    }

    private OstiumAuth(final Class<?> clazz) {
        this.clazz = clazz;
        this.logger = Annal.get(clazz);
        this.methods = clazz.getDeclaredMethods();
    }

    public OstiumAuth verify() {

        return this;
    }

    public void mount(final Cliff reference) {
        
    }
}
