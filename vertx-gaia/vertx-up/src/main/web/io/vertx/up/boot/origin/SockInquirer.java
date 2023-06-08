package io.vertx.up.boot.origin;

import io.horizon.eon.VMessage;
import io.horizon.uca.log.Annal;
import io.vertx.up.annotations.Broker;
import io.vertx.up.atom.worker.Remind;
import io.vertx.up.boot.parallel.SockThread;
import io.vertx.up.fn.Fn;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class SockInquirer implements Inquirer<Set<Remind>> {
    private static final Annal LOGGER = Annal.get(SockInquirer.class);

    @Override
    public Set<Remind> scan(final Set<Class<?>> clazzes) {
        final Set<Class<?>> endpoints = clazzes.stream()
            .filter(this::isSocked)
            .collect(Collectors.toSet());
        LOGGER.info(VMessage.Inquirer.WEBSOCKET, endpoints.size());
        final List<SockThread> threadReference = new ArrayList<>();
        /* 2.1.Build Api metadata **/
        for (final Class<?> endpoint : endpoints) {
            final SockThread thread =
                new SockThread(endpoint);
            threadReference.add(thread);
            thread.start();
        }
        /* 3.2. Join **/
        Fn.jvmAt(() -> {
            for (final SockThread item : threadReference) {
                item.join();
            }
        }, LOGGER);
        /* 3.3. Finally **/
        final Set<Remind> events = new HashSet<>();
        Fn.jvmAt(() -> {
            for (final SockThread item : threadReference) {
                events.addAll(item.getEvents());
            }
        }, LOGGER);
        return events;
    }

    private boolean isSocked(final Class<?> clazz) {
        final Method[] methods = clazz.getDeclaredMethods();
        final long counter = Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(Broker.class))
            .count();
        return 0 < counter;
    }
}
