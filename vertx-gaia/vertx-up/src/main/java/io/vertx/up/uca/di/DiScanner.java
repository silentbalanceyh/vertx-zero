package io.vertx.up.uca.di;

import io.vertx.up.eon.Plugins;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.util.Ut;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * New structure for JSR310 / JSR299
 * In future
 * - Now: Set terminal
 * - Future: Scc calculation extension
 */
public class DiScanner {

    private static final ConcurrentMap<Class<?>, ConcurrentMap<String, Class<?>>>
            PENDINGS = ZeroAnno.getPlugins();

    private static final DiAnchor INJECTOR = new DiAnchor(DiScanner.class);

    private final transient Annal logger;

    private DiScanner(final Class<?> callee) {
        this.logger = Annal.get(callee);
    }

    public static DiScanner create(final Class<?> callee) {
        return Fn.pool(Pool.INJECTION, callee, () -> new DiScanner(callee));
    }

    @SuppressWarnings("all")
    public <T> T singleton(final Class<?> instanceCls) {
        /*
         * Initialize object
         */
        final Object instance = Ut.singleton(instanceCls);
        if (Objects.nonNull(instance)) {
            /*
             * The method splitted because of future
             */
            this.singleton(instance);
        }
        return (T) instance;
    }

    public void singleton(final Object instance) {
        /*
         * JSR299 / 310 injection
         */
        final Class<?> clazz = instance.getClass();
        if (PENDINGS.containsKey(clazz)) {
            /*
             * Scanned in started up for target
             */
            final ConcurrentMap<String, Class<?>> injections =
                    PENDINGS.getOrDefault(clazz, new ConcurrentHashMap<>());
            injections.forEach((fieldName, type) ->
                    /*
                     * initialization for field injection
                     */
                    this.singleton(instance, fieldName, type));
        }
    }

    private void singleton(final Object proxy, final String fieldName, final Class<?> type) {
        try {
            final Class<?> clazz = proxy.getClass();
            final Field field = clazz.getDeclaredField(fieldName);
            final Object next;
            if (Plugins.INFIX_MAP.keySet().stream().anyMatch(field::isAnnotationPresent)) {
                /*
                 * @Mongo
                 * @MySql
                 * @Jooq
                 * @Rpc
                 * @Redis
                 */
                next = INJECTOR.initialize(field);
            } else {
                /*
                 * Inject Only
                 */
                next = Ut.singleton(type);
            }
            /*
             * Set for field get value here
             */
            if (Objects.nonNull(next)) {
                Ut.field(proxy, fieldName, next);
                /*
                 * Loop for continue injection
                 */
                this.singleton(next);
            }
        } catch (final NoSuchFieldException ex) {
            this.logger.jvm(ex);
        }
    }
}
