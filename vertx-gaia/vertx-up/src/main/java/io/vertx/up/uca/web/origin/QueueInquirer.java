package io.vertx.up.uca.web.origin;

import io.reactivex.Observable;
import io.vertx.core.eventbus.Message;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.eon.Info;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.zero.exception.WorkerConflictException;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is For annotation @Queue scanning
 * It will scan all classes that annotated with @Queue, zero system
 * will extract worker class from this scanned classes.
 */
public class QueueInquirer implements Inquirer<Set<Class<?>>> {
    private static final Annal LOGGER = Annal.get(QueueInquirer.class);

    @Override
    public Set<Class<?>> scan(final Set<Class<?>> classes) {
        final Set<Class<?>> queues = classes.stream()
            .filter((item) -> item.isAnnotationPresent(Queue.class))
            .collect(Collectors.toSet());
        LOGGER.info(Info.SCANED_QUEUE, queues.size());
        this.ensure(queues);
        return queues;
    }

    private void ensure(final Set<Class<?>> clazzes) {
        Observable.fromIterable(clazzes)
            .map(Class::getDeclaredMethods)
            .flatMap(Observable::fromArray)
            .filter(method -> method.isAnnotationPresent(Address.class))
            .subscribe(method -> {
                final Class<?> returnType = method.getReturnType();
                final Class<?> parameterTypes = method.getParameterTypes()[0];
                if (Message.class.isAssignableFrom(parameterTypes)) {
                    Fn.outUp(void.class != returnType && Void.class != returnType, LOGGER,
                        WorkerConflictException.class, this.getClass(), method);
                }
            })
            .dispose();
    }
}
