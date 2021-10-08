package io.vertx.up.uca.web.origin;

import io.reactivex.Observable;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.annotations.Queue;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Deprecated
public class PointerInquirer implements Inquirer<Set<Class<?>>> {

    @Override
    public Set<Class<?>> scan(final Set<Class<?>> allClasses) {
        final Set<Class<?>> pointers = new HashSet<>();
        // Filter Queue & Event
        Observable.fromIterable(allClasses)
            .filter(clazz -> !clazz.isAnnotationPresent(Queue.class) &&
                !clazz.isAnnotationPresent(EndPoint.class))
            .filter(this::isValid)
            .subscribe(pointers::add)
            .dispose();
        return pointers;
    }

    private boolean isValid(final Class<?> clazz) {
        final Field[] fields = clazz.getDeclaredFields();
        final Long counter = Observable.fromArray(fields)
            .filter(field -> field.isAnnotationPresent(Inject.class))
            .count().blockingGet();
        return 0 < counter;
    }
}
