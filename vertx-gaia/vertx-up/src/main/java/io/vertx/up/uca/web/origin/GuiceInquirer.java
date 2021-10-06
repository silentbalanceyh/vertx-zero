package io.vertx.up.uca.web.origin;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.annotations.Queue;
import io.vertx.up.log.Annal;
import io.vertx.up.plugin.Infix;
import io.vertx.up.uca.di.JavaDi;
import io.vertx.up.uca.di.JsrDi;
import io.vertx.up.uca.web.filter.HttpFilter;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class GuiceInquirer implements Inquirer<Injector> {
    private static final Annal LOGGER = Annal.get(GuiceInquirer.class);

    @Override
    @SuppressWarnings("all")
    public Injector scan(final Set<Class<?>> clazzes) {
        LOGGER.info("[ DI ] The DI environment will be initialized!");
        /*
         *  implement class = interface class ( set )
         */
        LOGGER.info("[ DI ] Start to parsing IMPL mode...");
        final ConcurrentMap<Class<?>, Set<Class<?>>> implMap = new ConcurrentHashMap<>();
        clazzes.forEach(clazz -> {
            if (this.isValid(clazz)) {
                if (!clazz.isInterface()) {
                    final Set<Class<?>> interfaces = Arrays.stream(clazz.getInterfaces())
                        .filter(item -> Serializable.class != item)
                        .collect(Collectors.toSet());
                    if (0 == interfaces.size()) {
                        implMap.put(clazz, new HashSet<>() {{
                            this.add(clazz);
                        }});
                    } else {
                        implMap.put(clazz, interfaces);
                    }
                }
            }
        });
        LOGGER.info("[ DI ] Start to parsing INTERFACE/INFIX mode...");
        final ConcurrentMap<Class<?>, Set<Class<?>>> interfaceMap = new ConcurrentHashMap<>();
        final ConcurrentMap<Class<?>, Class<?>> infixMap = new ConcurrentHashMap<>();
        implMap.forEach((impl, interfaceSet) -> interfaceSet.forEach(interfaceCls -> {
            if (interfaceCls.isInterface()) {
                if (Infix.class == interfaceCls) {
                    infixMap.put(impl, interfaceCls);
                } else {
                    final Set<Class<?>> interfaceValue = interfaceMap.getOrDefault(interfaceCls, new HashSet<>());
                    interfaceValue.add(impl);
                    interfaceMap.put(interfaceCls, interfaceValue);
                }
            }
        }));
        LOGGER.info("[ DI ] Duplicated removing...");
        interfaceMap.forEach((interfaceCls, implSet) -> {
            if (1 < implSet.size()) {
                implSet.forEach(implMap::remove);
            }
        });
        infixMap.keySet().forEach(implMap::remove);
        LOGGER.info("[ DI ] Calculate final size: IMPL = {0}, INTERFACE = {1}, INFIX = {2}",
            String.valueOf(implMap.size()), String.valueOf(interfaceMap.size()), String.valueOf(infixMap.size()));
        /*
         * Common Binding:
         * 1. Class Standalone
         * 2. Interface + Class
         */
        return Guice.createInjector(
            new JavaDi(implMap),        // Java Specification ( IMPL Mode )
            new JsrDi(interfaceMap)     // Jsr Specification ( interface Map )
        );
    }

    private boolean isValid(final Class<?> clazz) {
        final int modifier = clazz.getModifiers();
        boolean valid = Modifier.isPublic(modifier);
        // Non Public
        if (Modifier.isAbstract(modifier) && !clazz.isInterface()) {
            // Abstract Class Exclude
            valid = false;
        }
        if (clazz.isAnnotationPresent(EndPoint.class)
            || clazz.isAnnotationPresent(Queue.class)) {
            // Zero Component Exclude
            valid = false;
        }
        if (!Ut.withNoArgConstructor(clazz)) {
            // Fix: does not have a @Inject annotated constructor or a no-arg constructor.
            valid = false;
        }
        if (AbstractModule.class.isAssignableFrom(clazz)) {
            // Self Scan
            valid = false;
        }
        if (HttpFilter.class.isAssignableFrom(clazz)) {
            // Zero HttpFilter ignored
            valid = false;
        }
        return valid;
    }
}
