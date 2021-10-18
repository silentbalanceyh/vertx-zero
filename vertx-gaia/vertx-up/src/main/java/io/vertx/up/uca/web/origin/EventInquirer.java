package io.vertx.up.uca.web.origin;

import io.vertx.up.atom.agent.Event;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.web.thread.EndPointThread;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class EventInquirer implements Inquirer<Set<Event>> {

    private static final Annal LOGGER = Annal.get(EventInquirer.class);

    @Override
    public Set<Event> scan(final Set<Class<?>> endpoints) {
        final List<EndPointThread> threadReference = new ArrayList<>();
        /* 2.1.Build Api metadata **/
        for (final Class<?> endpoint : endpoints) {
            final EndPointThread thread =
                new EndPointThread(endpoint);
            threadReference.add(thread);
            thread.start();
        }
        /* 3.2. Join **/
        Fn.safeJvm(() -> {
            for (final EndPointThread item : threadReference) {
                item.join();
            }
        }, LOGGER);
        /* 3.3. Finally **/
        final Set<Event> events = new HashSet<>();
        Fn.safeJvm(() -> {
            for (final EndPointThread item : threadReference) {
                events.addAll(item.getEvents());
            }
        }, LOGGER);
        return events;
    }
}
