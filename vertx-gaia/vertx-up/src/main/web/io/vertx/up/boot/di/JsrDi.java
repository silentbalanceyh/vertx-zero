package io.vertx.up.boot.di;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.horizon.uca.log.Annal;
import io.vertx.up.util.Ut;
import jakarta.inject.Named;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Prepared
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Deprecated
public class JsrDi<I, T extends I> extends AbstractModule {
    private static final Annal LOGGER = Annal.get(JsrDi.class);
    private final transient ConcurrentMap<Class<I>, Set<Class<T>>> classes
        = new ConcurrentHashMap<>();

    public JsrDi(final ConcurrentMap<Class<I>, Set<Class<T>>> classes) {
        this.classes.putAll(classes);
    }

    @Override
    protected void configure() {
        LOGGER.info("[ DI ] Jsr Bind Start......");
        final Set<String> ignored = new HashSet<>();
        this.classes.forEach((interfaceCls, implSet) -> {
            if (1 == implSet.size()) {
                final Class<T> impl = implSet.iterator().next();
                LOGGER.info("[ DI ] 1 --> 1, Interface clazz bind = {0}, interface = {1}", impl, interfaceCls);
                this.bind(interfaceCls).to(impl).asEagerSingleton();
            } else {
                implSet.forEach(implCls -> {
                    if (implCls.isAnnotationPresent(Named.class)) {
                        LOGGER.info("[ DI ] 1 --> 1, Interface named bind = {0}, interface = {1}", implCls, interfaceCls);
                        final Annotation annotation = implCls.getAnnotation(Named.class);
                        final String name = Ut.invoke(annotation, "value");
                        this.bind(interfaceCls).annotatedWith(Names.named(name))
                            .to(implCls).asEagerSingleton();
                    } else {
                        ignored.add(implCls.getName());
                    }
                });
            }
        });
        LOGGER.info("[ DI ] ?, Size = {0}, No definition!!!! Impl = {1}",
            String.valueOf(ignored.size()), Ut.fromJoin(ignored));
    }
}
