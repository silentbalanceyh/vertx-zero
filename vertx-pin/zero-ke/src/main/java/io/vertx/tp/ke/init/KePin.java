package io.vertx.tp.ke.init;

import io.vertx.tp.optic.atom.Lexeme;
import io.vertx.up.log.Annal;

import java.util.Iterator;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Cross Infix management here as bus
 * It's defined interface / abstract only and management uniform
 */
public class KePin {

    private static final Annal LOGGER = Annal.get(KePin.class);

    private static final ConcurrentMap<String, Object> REF =
        new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> Lexeme<T> get(final Class<T> interfaceCls) {
        if (Objects.isNull(interfaceCls)) {
            return null;
        } else {
            final Object original = REF.getOrDefault(interfaceCls.getName(), null);
            if (Objects.isNull(original)) {
                /*
                 * Service Loader for lookup input interface implementation
                 * This configuration must be configured in
                 * META-INF/services/<interfaceCls Name> file
                 */
                final ServiceLoader<T> loader =
                    ServiceLoader.load(interfaceCls, interfaceCls.getClassLoader());

                /*
                 * New data structure to put interface class into LEXEME_MAP
                 * In current version, it support one to one only
                 *
                 * 1) The key is interface class name
                 * 2) The found class is implementation name
                 */
                final Iterator<T> it = loader.iterator();
                Lexeme<T> found = null;
                while (it.hasNext()) {

                    /*
                     * Found the first implementation class
                     */
                    final T reference = it.next();
                    if (Objects.nonNull(reference)) {
                        found = new Lexeme<>(interfaceCls, reference);
                        /*
                         * Refresh cache
                         */
                        final String cacheKey = interfaceCls.getName();
                        LOGGER.info("Lexeme<T>: interface = {0} <-------- impl = {1}",
                            cacheKey, reference.getClass().getName());
                        REF.put(cacheKey, found);
                        break;
                    }
                }
                return found;
            } else {
                /*
                 * Found in cache and return (Lexeme<T>) directly
                 */
                return (Lexeme<T>) original;
            }
        }
    }
}
