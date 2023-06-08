package io.vertx.up.boot.di;

import com.google.inject.AbstractModule;
import jakarta.inject.Inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public class DiGuiceConstructor<T extends I, I> implements DiGuice<T, I> {
    private final transient Set<Class<?>> pointers = new HashSet<>();

    @Override
    public boolean success(final Class<?> clazz) {
        // Get all Constructor
        final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        return Arrays.stream(constructors)
            .filter(constructor -> !Modifier.isPublic(constructor.getModifiers()))   // Ko Non-Public
            .filter(constructor -> 0 < constructor.getParameterCount())              // Ko ()
            .filter(constructor -> constructor.isAnnotationPresent(Inject.class))    // JSR 330
            .anyMatch(constructor -> {
                final Class<?>[] parameters = constructor.getParameterTypes();
                this.pointers.addAll(Arrays.asList(parameters));
                return true;
            });
    }

    @Override
    public AbstractModule module(final ConcurrentMap<Class<I>, Set<Class<T>>> tree,
                                 final Set<Class<T>> flat) {
        return new AbstractModule() {
            @Override
            protected void configure() {
            }
        };
    }
}
