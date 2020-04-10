package io.vertx.up.uca.web.origin;

import io.vertx.up.annotations.Worker;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Worker
 */
public class WorkerInquirer implements Inquirer<Set<Class<?>>> {

    @Override
    public Set<Class<?>> scan(final Set<Class<?>> classes) {
        return classes.stream()
                .filter((item) -> item.isAnnotationPresent(Worker.class))
                .collect(Collectors.toSet());
    }
}
