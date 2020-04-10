package io.vertx.tp.optic;

import io.vertx.tp.ke.init.KePin;
import io.vertx.tp.optic.atom.Income;
import io.vertx.tp.optic.atom.Lexeme;
import io.vertx.up.util.Ut;

import java.util.Objects;

/*
 * T seeking in the environment of kernel.
 */
public class Pocket {
    /*
     * Lookup interface
     */
    public static <T> T lookup(final Class<?> clazz) {
        /*
         * Get lexeme reference here.
         */
        final Lexeme lexeme = KePin.get(clazz);
        if (Objects.isNull(lexeme)) {
            /*
             * Null pointer
             */
            return null;
        } else {
            /*
             * Implementation pointer
             */
            final Class<?> implCls = lexeme.getImplCls();
            return Ut.singleton(implCls);
        }
    }

    public static Income income(final Class<?> clazz, final Object... args) {
        final Income income = Income.in(clazz);
        for (final Object arg : args) {
            income.in(arg);
        }
        return income;
    }
}
