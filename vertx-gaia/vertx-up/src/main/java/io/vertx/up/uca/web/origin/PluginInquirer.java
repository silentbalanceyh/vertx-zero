package io.vertx.up.uca.web.origin;

import io.reactivex.Observable;
import io.vertx.up.annotations.Plugin;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class PluginInquirer implements Inquirer<Set<Class<?>>> {

    @Override
    public Set<Class<?>> scan(final Set<Class<?>> allClasses) {
        final Set<Class<?>> plugins = new HashSet<>();
        // Filter Client
        Observable.fromIterable(allClasses)
            .filter(this::isPlugin)
            .subscribe(plugins::add)
            .dispose();
        // Ensure Tp Client
        return plugins;
    }

    private boolean isPlugin(final Class<?> clazz) {
        final Field[] fields = clazz.getDeclaredFields();
        final Long counter = Observable.fromArray(fields)
            .filter(field -> field.isAnnotationPresent(Plugin.class))
            .count().blockingGet();
        return 0 < counter;
    }
}
