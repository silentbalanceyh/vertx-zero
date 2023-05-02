package io.horizon.fn;

import io.horizon.exception.BootingException;
import io.horizon.exception.ProgramException;
import io.horizon.exception.WebException;
import io.horizon.exception.web._412ArgumentNullException;
import io.horizon.specification.uca.HLogger;
import io.horizon.uca.log.Annal;
import io.horizon.util.HUt;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author lang : 2023/4/28
 */
class HThrow {
    /*
     * - WebException
     * - BootingException
     */
    static void out(final Class<?> errorCls, final Object... args) {
        if (BootingException.class == errorCls.getSuperclass()) {
            final BootingException error = HUt.instance(errorCls, args);
            if (null != error) {
                callerAt(error::caller, error::getMessage);
                throw error;
            }
        } else if (WebException.class == errorCls.getSuperclass()) {
            final WebException error = HUt.instance(errorCls, args);
            if (null != error) {
                callerAt(error::caller, error::getMessage);
                throw error;
            }
        }
    }

    static void outBug(final HLogger logger,
                       final Class<? extends ProgramException> zeroClass,
                       final Object... args) throws ProgramException {
        final ProgramException error = HUt.instance(zeroClass, args);
        if (null != error) {
            if (Objects.nonNull(logger)) {
                logger.fatal(error);
            }
            throw error;
        }
    }

    static void outBug(final HLogger logger, final ProgramActuator actuator) {
        try {
            actuator.execute();
        } catch (final ProgramException ex) {
            if (Objects.nonNull(logger)) {
                logger.fatal(ex);
            }
            // 抛出合法异常
        } catch (final Throwable ex) {
            if (Objects.nonNull(logger)) {
                logger.fatal(ex);
            }
        }
    }

    static void outWeb(final Class<? extends WebException> webClass,
                       final Object... args) {
        final WebException error = HUt.instance(webClass, args);
        if (null != error) {
            callerAt(error::caller, error::getMessage);
            throw error;
        }
    }

    static void outWeb(final HLogger logger,
                       final Class<? extends WebException> webClass,
                       final Object... args) {
        final WebException error = HUt.instance(webClass, args);
        if (null != error) {
            if (Objects.nonNull(logger)) {
                logger.warn(error.getMessage());
            }
            throw error;
        }
    }

    static void outBoot(final HLogger logger,
                        final Class<? extends BootingException> upClass,
                        final Object... args) {
        final BootingException error = HUt.instance(upClass, args);
        if (null != error) {
            if (Objects.nonNull(logger)) {
                logger.fatal(error);
            }
            throw error;
        }
    }

    static void outBoot(final Class<? extends BootingException> upClass,
                        final Object... args) {
        final BootingException error = HUt.instance(upClass, args);
        if (null != error) {
            callerAt(error::caller, error::getMessage);
            throw error;
        }
    }

    static <T> void outArg(final T condition, final Class<?> clazz, final String message) {
        if (condition instanceof final Boolean check) {
            // If boolean, condition = true, throw Error
            if (check) {
                outWeb(_412ArgumentNullException.class, clazz, message);
            }
        } else if (condition instanceof final String check) {
            // If string, condition = empty or null, throw Error
            if (HUt.isNil(check)) {
                outWeb(_412ArgumentNullException.class, clazz, message);
            }
        } else if (Objects.isNull(condition)) {
            // If object, condition = null, throw Error
            outWeb(_412ArgumentNullException.class, clazz, message);
        }
    }

    private static void callerAt(final Supplier<Class<?>> supplier, final Supplier<String> message) {
        final Class<?> target = supplier.get();
        if (null != target) {
            final Annal logger = Annal.get(target);
            logger.warn(message.get());
        }
    }
}
