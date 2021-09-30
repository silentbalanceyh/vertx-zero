package io.vertx.up.uca.web.origin;

import io.vertx.up.eon.Info;
import io.vertx.up.eon.Plugins;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.Anno;
import io.vertx.up.uca.web.thread.AffluxThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Deprecated
public class AffluxInquirer implements
    Inquirer<ConcurrentMap<Class<?>, ConcurrentMap<String, Class<?>>>> {

    private static final Annal LOGGER = Annal.get(AffluxInquirer.class);

    @Override
    public ConcurrentMap<Class<?>, ConcurrentMap<String, Class<?>>> scan(final Set<Class<?>> classes) {
        // Find condition ok
        final Set<Class<?>> enabled = classes.stream()
            /*
             * @Mongo
             * @MySql
             * @Jooq
             * @Rpc
             * @Redis
             * @Inject ( JSR 299 )
             */
            .filter(item -> Anno.isMark(item, Plugins.INJECT_ANNOTATIONS))
            .collect(Collectors.toSet());
        // Scan each class.
        final List<AffluxThread> threadReference = new ArrayList<>();
        for (final Class<?> clazz : enabled) {
            final AffluxThread thread = new
                AffluxThread(clazz, classes);
            threadReference.add(thread);
            thread.start();
        }
        threadReference.forEach(thread -> {
            try {
                thread.join();
            } catch (final InterruptedException ex) {
                LOGGER.jvm(ex);
            }
        });
        final ConcurrentMap<Class<?>, ConcurrentMap<String, Class<?>>> affluxes
            = new ConcurrentHashMap<>();
        for (final AffluxThread thread : threadReference) {
            if (!thread.isEmpty()) {
                final Class<?> key = thread.getClassKey();
                final ConcurrentMap<String, Class<?>> fields = thread.getFieldMap();
                affluxes.put(key, fields);
                LOGGER.info(Info.SCANED_INJECTION, key.getName(), fields.size());
            }
        }
        return affluxes;
    }
}
