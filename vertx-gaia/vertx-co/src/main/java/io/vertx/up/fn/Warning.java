package io.vertx.up.fn;

import io.horizon.exception.AbstractException;
import io.horizon.exception.ProgramException;
import io.vertx.up.exception.UpException;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._412NullValueException;
import io.horizon.uca.log.Annal;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Announce means tell every one of Zero system that there occurs error, the error contains
 * 1. java.lang.Exception ( Checked )
 * 2. io.horizon.exception.ZeroException ( Checked )
 * 3. java.lang.Throwable ( Runtime )
 * 4. io.horizon.exception.ZeroRunException ( Runtime )
 */
final class Warning {
    private Warning() {
    }

    /**
     * Build zero error and throw ZeroException out.
     *
     * @param logger    Zero Logger
     * @param zeroClass ZeroException class
     * @param args      Arguments of zero
     *
     * @throws ProgramException Whether throw out exception of zero defined.
     */
    static void outZero(final Annal logger,
                        final Class<? extends ProgramException> zeroClass,
                        final Object... args)
        throws ProgramException {
        final ProgramException error = Ut.instance(zeroClass, args);
        if (null != error) {
            logger.fatal(error);
            //            Annal.sure(logger, () -> logger.checked(error));
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
                      final Class<? extends AbstractException> upClass,
                      final Object... args) {
        final AbstractException error = Ut.instance(upClass, args);
        if (null != error) {
            logger.fatal(error);
            //            Annal.sure(logger, () -> logger.runtime(error));
            throw error;
        }
    }

    static void outWeb(final Annal logger,
                       final Class<? extends WebException> webClass,
                       final Object... args) {
        final WebException error = Ut.instance(webClass, args);
        if (null != error) {
            logger.warn(error.getMessage());
            //            Annal.sure(logger, () -> logger.warn(error.getMessage()));
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

    static <T> void outOr(final T condition, final Class<?> clazz, final String message) {
        if (condition instanceof final Boolean check) {
            // If boolean, condition = true, throw Error
            if (check) {
                outWeb(_412NullValueException.class, clazz, message);
            }
        } else if (condition instanceof final String check) {
            // If string, condition = empty or null, throw Error
            if (Ut.isNil(check)) {
                outWeb(_412NullValueException.class, clazz, message);
            }
        } else if (Objects.isNull(condition)) {
            // If object, condition = null, throw Error
            outWeb(_412NullValueException.class, clazz, message);
        }
    }

    private static void sure(final Supplier<Class<?>> supplier, final Supplier<String> message) {
        final Class<?> target = supplier.get();
        if (null != target) {
            final Annal logger = Annal.get(target);
            logger.warn(message.get());
        }
    }


    /**
     * @param supplier T supplier function
     * @param runCls   ZeroRunException definition
     * @param <T>      T type of object
     *
     * @return Final T or throw our exception
     */
    static <T> T execRun(final Supplier<T> supplier, final Class<? extends AbstractException> runCls, final Object... args) {
        T ret = null;
        try {
            ret = supplier.get();
        } catch (final Throwable ex) {
            final Object[] argument = Ut.elementAdd(args, ex);
            final AbstractException error = Ut.instance(runCls, argument);
            if (null != error) {
                throw error;
            }
        }
        return ret;
    }
}
