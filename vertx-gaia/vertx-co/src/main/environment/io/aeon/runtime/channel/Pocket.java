package io.aeon.runtime.channel;

import io.aeon.atom.channel.KIncome;
import io.aeon.atom.channel.KLexeme;
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
    public static <T> KLexeme<T> get(final Class<T> interfaceCls) {
        if (Objects.isNull(interfaceCls)) {
            return null;
        } else {
            /* Provide Overwrite Structure for channel processing */
            final String cacheKey = interfaceCls.getName();
            Object found = REF.getOrDefault(cacheKey, null);
            if (Objects.isNull(found)) {
                final T reference = Ut.serviceChannel(interfaceCls);
                if (Objects.nonNull(reference)) {
                    found = new KLexeme<>(interfaceCls, reference);
                    REF.put(cacheKey, found);
                }
            }
            return (KLexeme<T>) found;
        }
    }

    /*
     * Lookup interface
     */
    public static <T> T lookup(final Class<T> clazz) {
        /*
         * Get lexeme reference here.
         */
        final KLexeme<T> lexeme = get(clazz);
        if (Objects.isNull(lexeme)) {
            /*
             * Null dot
             */
            return null;
        } else {
            /*
             * Implementation dot
             */
            return lexeme.instance();
        }
    }

    public static <T> KIncome income(final Class<T> clazz, final Object... args) {
        final KIncome income = KIncome.in(clazz);
        for (final Object arg : args) {
            income.in(arg);
        }
        return income;
    }
}
