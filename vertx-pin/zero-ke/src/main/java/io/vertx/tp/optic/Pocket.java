package io.vertx.tp.optic;

import io.vertx.tp.ke.init.KePin;
import io.vertx.tp.optic.atom.Income;
import io.vertx.tp.optic.atom.Lexeme;

import java.util.Objects;

/*
 * T seeking in the environment of kernel.
 */
public class Pocket {
    /*
     * Lookup interface
     */
    public static <T> T lookup(final Class<T> clazz) {
        /*
         * Get lexeme reference here.
         */
        final Lexeme<T> lexeme = KePin.get(clazz);
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
