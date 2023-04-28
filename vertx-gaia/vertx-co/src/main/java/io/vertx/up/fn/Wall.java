package io.vertx.up.fn;

import io.horizon.eon.VValue;
import io.horizon.exception.AbstractException;
import io.horizon.exception.ProgramException;
import io.horizon.fn.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.log.Annal;

import java.util.function.Supplier;

/**
 * Defend means swapper the exception part for specific statement.
 * Uniform to manage exception code flow.
 */
final class Wall {
    private Wall() {
    }

    /**
     * Execute without any return type
     *
     * @param actuator Jvm Executor
     * @param logger   Zero logger
     */
    static void jvmVoid(final ExceptionActuator actuator, final Annal logger) {
        try {
            actuator.execute();
        } catch (final Throwable ex) {
            logger.fatal(ex);
            //            Annal.sure(logger, () -> logger.jvm(ex));
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
     *
     * @return T supplier or null
     */
    static <T> T jvmReturn(final ExceptionSupplier<T> supplier, final Annal logger) {
        T reference = null;
        try {
            reference = supplier.get();
        } catch (final Exception ex) {
            logger.fatal(ex);
            //            Annal.sure(logger, () -> logger.jvm(ex));
            // TODO: Debug for JVM
            ex.printStackTrace();
        }
        return reference;
    }

    /**
     * @param actuator Zero executor
     * @param logger   Zero logger
     */
    static void zeroVoid(final ProgramActuator actuator, final Annal logger) {
        try {
            actuator.execute();
        } catch (final ProgramException ex) {
            logger.fatal(ex);
            //            Annal.sure(logger, () -> logger.checked(ex));
        } catch (final AbstractException ex) {
            logger.fatal(ex);
            //            Annal.sure(logger, () -> logger.runtime(ex));
        } catch (final Throwable ex) {
            logger.fatal(ex);
            //            Annal.sure(logger, () -> logger.jvm(ex));
            // TODO: Debug for JVM
            ex.printStackTrace();
        }
    }

    /**
     * @param supplier Zero Supplier
     * @param logger   Zero Logger
     * @param <T>      Element of supplier ( T )
     *
     * @return T or throw out zero run exception
     */
    static <T> T zeroReturn(final ProgramSupplier<T> supplier, final Annal logger) {
        T ret = null;
        try {
            ret = supplier.get();
        } catch (final ProgramException ex) {
            logger.fatal(ex);
            //            Annal.sure(logger, () -> logger.checked(ex));
        } catch (final AbstractException ex) {
            logger.fatal(ex);
            throw ex;
        } catch (final Throwable ex) {
            logger.fatal(ex);
            //            Annal.sure(logger, () -> logger.jvm(ex));
            // TODO: Debug for JVM
            ex.printStackTrace();
        }
        return ret;
    }


    static <T> T execReturn(final Supplier<T> supplier, final T defaultValue) {
        final T ret = supplier.get();
        return null == ret ? defaultValue : ret;
    }

    static void exec(final boolean condition, final Annal logger, final Actuator tSupplier, final Actuator fSupplier) {
        Wall.zeroVoid(() -> execZero(condition,
            () -> {
                if (null != tSupplier) {
                    tSupplier.execute();
                }
                return null;
            }, () -> {
                if (null != fSupplier) {
                    fSupplier.execute();
                }
                return null;
            }), logger);
    }


    @SuppressWarnings("all")
    static <T> T execZero(final boolean condition, final ProgramSupplier<T> tSupplier, final ProgramSupplier<T> fSupplier)
        throws ProgramException {
        T ret = null;
        if (condition) {
            if (null != tSupplier) {
                ret = tSupplier.get();
            }
        } else {
            if (null != fSupplier) {
                ret = fSupplier.get();
            }
        }
        return ret;
    }

    @SuppressWarnings("all")
    static <T> void execZero(final JsonObject data, final ProgramBiConsumer<T, String> fnIt)
        throws ProgramException {
        for (final String name : data.fieldNames()) {
            final Object item = data.getValue(name);
            if (null != item) {
                fnIt.accept((T) item, name);
            }
        }
    }

    /**
     * @param dataArray JsonArray that will be iterated
     * @param fnIt      iterator
     * @param <T>       element type
     */
    static <T> void execZero(final JsonArray dataArray, final ProgramBiConsumer<T, String> fnIt)
        throws ProgramException {
        execZero(dataArray, JsonObject.class, (element, index) -> execZero(element, fnIt));
    }

    /**
     * @param dataArray JsonArray that will be iterated
     * @param clazz     expected class
     * @param fnIt      iterator
     * @param <T>       element type T ( generic )
     *
     * @throws ProgramException element zero here
     */
    @SuppressWarnings("all")
    static <T> void execZero(final JsonArray dataArray, final Class<T> clazz, final ProgramBiConsumer<T, Integer> fnIt)
        throws ProgramException {
        final int size = dataArray.size();
        for (int idx = VValue.IDX; idx < size; idx++) {
            final Object value = dataArray.getValue(idx);
            if (null != value) {
                if (clazz == value.getClass()) {
                    final T item = (T) value;
                    fnIt.accept(item, idx);
                }
            }
        }
    }
}
