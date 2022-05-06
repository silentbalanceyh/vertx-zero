package io.vertx.up.experiment.channel;

import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * T seeking in the environment of kernel.
 */
public class Pocket {
    private static final ConcurrentMap<String, Object> REF =
        new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> Lexeme<T> get(final Class<T> interfaceCls) {
        if (Objects.isNull(interfaceCls)) {
            return null;
        } else {
            final String cacheKey = interfaceCls.getName();
            Object found = REF.getOrDefault(cacheKey, null);
            if (Objects.isNull(found)) {
                final T reference = Ut.service(interfaceCls);
                if (Objects.nonNull(reference)) {
                    found = new Lexeme<>(interfaceCls, reference);
                    REF.put(cacheKey, found);
                }
            }
            return (Lexeme<T>) found;
        }
    }

    /*
     * Lookup interface
     */
    public static <T> T lookup(final Class<T> clazz) {
        /*
         * Get lexeme reference here.
         */
        final Lexeme<T> lexeme = get(clazz);
        if (Objects.isNull(lexeme)) {
            /*
             * Null pointer
             */
            return null;
        } else {
            /*
             * Implementation pointer
             */
            return lexeme.instance();
        }
    }

    public static <T> Income income(final Class<T> clazz, final Object... args) {
        final Income income = Income.in(clazz);
        for (final Object arg : args) {
            income.in(arg);
        }
        return income;
    }
}
