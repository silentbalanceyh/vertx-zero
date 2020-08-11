package io.vertx.up.fn;

import io.vertx.core.VertxException;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.exception.ZeroRunException;
import io.vertx.up.log.Annal;

/**
 * Defend means swapper the exception part for specific statement.
 * Uniform to manage exception code flow.
 */
final class Defend {
    private Defend() {
    }

    /**
     * Execute without any return type
     *
     * @param actuator Jvm Executor
     * @param logger   Zero logger
     */
    static void jvmVoid(final JvmActuator actuator,
                        final Annal logger) {
        try {
            actuator.execute();
        } catch (final Throwable ex) {
            Annal.sure(logger, () -> logger.jvm(ex));
            // TODO: Debug for JVM
            ex.printStackTrace();
        }
    }

    /**
     * Execute with return T
     *
     * @param supplier Jvm Supplier
     * @param logger   Zero logger
     * @param <T>      returned supplier T
     * @return T supplier or null
     */
    static <T> T jvmReturn(final JvmSupplier<T> supplier,
                           final Annal logger) {
        T reference = null;
        try {
            reference = supplier.get();
        } catch (final Exception ex) {
            Annal.sure(logger, () -> logger.jvm(ex));
            // TODO: Debug for JVM
            ex.printStackTrace();
        }
        return reference;
    }

    /**
     * @param actuator Zero executor
     * @param logger   Zero logger
     */
    static void zeroVoid(final ZeroActuator actuator,
                         final Annal logger) {
        try {
            actuator.execute();
        } catch (final ZeroException ex) {
            Annal.sure(logger, () -> logger.zero(ex));
        } catch (final VertxException ex) {
            Annal.sure(logger, () -> logger.vertx(ex));
        } catch (final Throwable ex) {
            Annal.sure(logger, () -> logger.jvm(ex));
            // TODO: Debug for JVM
            ex.printStackTrace();
        }
    }

    /**
     * @param supplier Zero Supplier
     * @param logger   Zero Logger
     * @param <T>      Element of supplier ( T )
     * @return T or throw out zero run exception
     */
    static <T> T zeroReturn(final ZeroSupplier<T> supplier,
                            final Annal logger) {
        T ret = null;
        try {
            ret = supplier.get();
        } catch (final ZeroException ex) {
            Annal.sure(logger, () -> logger.zero(ex));
        } catch (final ZeroRunException ex) {
            Annal.sure(logger, () -> {
                logger.vertx(ex);
                throw ex;
            });
        } catch (final VertxException ex) {
            Annal.sure(logger, () -> logger.vertx(ex));
        } catch (final Throwable ex) {
            Annal.sure(logger, () -> logger.jvm(ex));
            // TODO: Debug for JVM
            ex.printStackTrace();
        }
        return ret;
    }
}
