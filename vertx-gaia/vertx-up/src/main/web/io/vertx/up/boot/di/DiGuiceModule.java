package io.vertx.up.boot.di;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.horizon.uca.log.Annal;
import io.vertx.up.util.Ut;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class DiGuiceModule extends AbstractModule {

    protected <T> String bindConstructor(final Class<T> clazz) {
        // Standalone, Non-Constructor
        if (Ut.isDefaultConstructor(clazz)) {
            final Constructor<T> constructor = Ut.constructor(clazz);
            if (clazz.isAnnotationPresent(Singleton.class)) {
                this.bind(clazz).toConstructor(constructor).asEagerSingleton();
            } else {
                this.bind(clazz).toConstructor(constructor);
            }
            this.logger().info("[ DI ] Constructor Bind: `{0}`", clazz);
            return null;
        } else {
            return clazz.getName();
        }
    }

    @SuppressWarnings("all")
    protected <T extends I, I> Set<String> bindInterface(final Class<I> interfaceCls, final Set<Class<T>> implSet) {
        if (!implSet.isEmpty()) {
            final Set<String> clazzSet = new HashSet<>();
            if (1 == implSet.size()) {
                final Class<T> clazz = implSet.iterator().next();
                this.bind(interfaceCls).to(clazz);
                this.logger().info("[ DI ] Interface Bind: `{0}`, interfaceCls = `{1}`", clazz, interfaceCls);
                // clazzSet.add(clazz.getName());
            } else {
                // interface with multi classed injection
                implSet.forEach(implCls -> {
                    if (implCls.isAnnotationPresent(Named.class)) {
                        final Annotation annotation = implCls.getAnnotation(Named.class);
                        final String name = Ut.invoke(annotation, "value");
                        this.logger().info("[ DI ] Interface Bind: `{0}`, interfaceCls = `{1}`, name = {2}",
                            implCls, interfaceCls, name);
                        this.bind(interfaceCls).annotatedWith(Names.named(name))
                            .to(implCls);
                    } else {
                        clazzSet.add(implCls.getName());
                    }
                });
            }
            return clazzSet;
        } else {
            return null;
        }
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}
