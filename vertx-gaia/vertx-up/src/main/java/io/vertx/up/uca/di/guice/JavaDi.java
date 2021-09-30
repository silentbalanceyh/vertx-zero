package io.vertx.up.uca.di.guice;

import com.google.inject.AbstractModule;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Prepared
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class JavaDi<T extends I, I> extends AbstractModule {
    private static final Annal LOGGER = Annal.get(JavaDi.class);
    private final transient ConcurrentMap<Class<T>, Set<Class<I>>> classes
        = new ConcurrentHashMap<>();

    public JavaDi(final ConcurrentMap<Class<T>, Set<Class<I>>> classes) {
        this.classes.putAll(classes);
    }

    @Override
    protected void configure() {
        LOGGER.info("[ DI ] Java Bind Start......");
        final Set<String> oneOne = new HashSet<>();
        this.classes.forEach((impl, interfaceSet) -> interfaceSet.forEach(interfaceCls -> {
            final Constructor<T> constructor = Ut.constructor(impl);
            if (Objects.nonNull(constructor)) {
                if (impl == interfaceCls) {
                    // No Interface Mode
                    oneOne.add(impl.getName());
                    this.bind(impl).toConstructor(constructor).asEagerSingleton();
                } else {
                    // Interface Mode ( More than one interface )
                    if (1 == interfaceSet.size()) {
                        LOGGER.info("[ DI ] 1 --> 1, Interface clazz bind = {0}, interface = {1}",
                            impl, interfaceCls);
                    } else {
                        LOGGER.info("[ DI ] 1 --> N, Interface clazz bind = {0}, interface = {1}",
                            impl, interfaceCls);
                    }
                    this.bind(interfaceCls).to(impl).asEagerSingleton();
                }
            }
        }));
        LOGGER.info("[ DI ] 0 <-> 0, Size = {0}, Content = {1}",
            String.valueOf(oneOne.size()), Ut.fromJoin(oneOne));
    }
}
