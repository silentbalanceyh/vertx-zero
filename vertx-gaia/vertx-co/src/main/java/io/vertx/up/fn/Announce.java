package io.vertx.up.fn;

import io.vertx.up.exception.WebException;
import io.vertx.up.log.Annal;
import io.vertx.up.exception.UpException;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.exception.ZeroRunException;
import io.vertx.up.util.Ut;

import java.util.function.Supplier;

/**
 * Announce means tell every one of Zero system that there occurs error, the error contains
 * 1. java.lang.Exception ( Checked )
 * 2. io.vertx.up.exception.ZeroException ( Checked )
 * 3. java.lang.Throwable ( Runtime )
 * 4. io.vertx.up.exception.ZeroRunException ( Runtime )
 */
final class Announce {
    private Announce() {
    }

    /**
     * Build zero error and throw ZeroException out.
     *
     * @param logger    Zero Logger
     * @param zeroClass ZeroException class
     * @param args      Arguments of zero
     * @throws ZeroException Whether throw out exception of zero defined.
     */
    static void outZero(final Annal logger,
                        final Class<? extends ZeroException> zeroClass,
                        final Object... args)
            throws ZeroException {
        final ZeroException error = Ut.instance(zeroClass, args);
        if (null != error) {
            Annal.sure(logger, () -> logger.zero(error));
            throw error;
        }
    }

    /**
     * Build up error and throw ZeroRunException out.
     *
     * @param logger  Zero Logger
     * @param upClass UpException class
     * @param args    Arguments of zero
     */
    static void outUp(final Annal logger,
                      final Class<? extends ZeroRunException> upClass,
                      final Object... args) {
        final ZeroRunException error = Ut.instance(upClass, args);
        if (null != error) {
            Annal.sure(logger, () -> logger.vertx(error));
            throw error;
        }
    }

    static void outWeb(final Annal logger,
                       final Class<? extends WebException> webClass,
                       final Object... args) {
        final WebException error = Ut.instance(webClass, args);
        if (null != error) {
            Annal.sure(logger, () -> logger.warn(error.getMessage()));
            throw error;
        }
    }

    /*
     * New Structure to avoid Annal logger created.
     * Uniform method for out exception
     * Here are two output
     * - WebException
     * - UpException
     */
    static void out(final Class<?> errorCls, final Object... args) {
        if (UpException.class == errorCls.getSuperclass()) {
            final UpException error = Ut.instance(errorCls, args);
            if (null != error) {
                sure(error::getTarget, error::getMessage);
                throw error;
            }
        } else if (WebException.class == errorCls.getSuperclass()) {
            final WebException error = Ut.instance(errorCls, args);
            if (null != error) {
                sure(error::getTarget, error::getMessage);
                throw error;
            }
        }
    }

    static void outUp(final Class<? extends UpException> upClass,
                      final Object... args) {
        final UpException error = Ut.instance(upClass, args);
        if (null != error) {
            sure(error::getTarget, error::getMessage);
            throw error;
        }
    }

    static void outWeb(final Class<? extends WebException> webClass,
                       final Object... args) {
        final WebException error = Ut.instance(webClass, args);
        if (null != error) {
            sure(error::getTarget, error::getMessage);
            throw error;
        }
    }

    private static void sure(final Supplier<Class<?>> supplier, final Supplier<String> message) {
        final Class<?> target = supplier.get();
        if (null != target) {
            final Annal logger = Annal.get(target);
            logger.warn(message.get());
        }
    }

    static void toRun(final Annal logger, final ZeroActuator actuator) {
        try {
            actuator.execute();
        } catch (final ZeroException ex) {
            Annal.sure(logger, () -> logger.zero(ex));
            throw new ZeroRunException(ex.getMessage()) {
            };
        } catch (final Throwable ex) {
            Annal.sure(logger, () -> logger.jvm(ex));
        }
    }

    /**
     * Execut actuator and throw ZeroRunException out
     *
     * @param actuator Executor of zero defined interface
     * @param logger   Zero logger
     */
    static void shuntRun(final Actuator actuator, final Annal logger) {
        try {
            actuator.execute();
        } catch (final ZeroRunException ex) {
            Annal.sure(logger, () -> logger.vertx(ex));
            throw ex;
        } catch (final Throwable ex) {
            Annal.sure(logger, () -> logger.jvm(ex));
        }
    }
}
