package io.vertx.up.boot.origin;

import io.vertx.up.annotations.Worker;

import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
public class WorkerInquirer implements Inquirer<Set<Class<?>>> {

    @Override
    public Set<Class<?>> scan(final Set<Class<?>> classes) {
        return classes.stream()
            .filter((item) -> item.isAnnotationPresent(Worker.class))
            .collect(Collectors.toSet());
    }
}
