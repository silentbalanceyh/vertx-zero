package io.vertx.up.boot.di;

import com.google.inject.AbstractModule;
import jakarta.inject.Inject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public class DiGuiceMethod<T extends I, I> implements DiGuice<T, I> {
    private final transient Set<Class<?>> pointers = new HashSet<>();

    @Override
    public boolean success(final Class<?> clazz) {
        // Get all Method
        final Method[] methods = clazz.getDeclaredMethods();
        return Arrays.stream(methods)
            .filter(method -> !Modifier.isStatic(method.getModifiers()))   // Ko Static
            .filter(method -> !Modifier.isPublic(method.getModifiers()))   // Ko Non-Public
            .filter(method -> 0 < method.getParameterCount())              // Ko ()
            .filter(method -> method.isAnnotationPresent(Inject.class))    // JSR 330
            .anyMatch(method -> {
                final Class<?>[] parameters = method.getParameterTypes();
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
