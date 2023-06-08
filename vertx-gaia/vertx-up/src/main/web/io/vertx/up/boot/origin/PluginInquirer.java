package io.vertx.up.boot.origin;

import io.reactivex.rxjava3.core.Observable;
import io.vertx.up.annotations.Infusion;

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
            .filter(field -> field.isAnnotationPresent(Infusion.class))
            .count().blockingGet();
        return 0 < counter;
    }
}
