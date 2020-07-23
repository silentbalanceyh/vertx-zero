package io.vertx.up.fn;

import io.vertx.up.exception.ZeroException;
import io.vertx.up.exception.ZeroRunException;
import io.vertx.up.log.Annal;
import io.vertx.up.log.Debugger;
import io.vertx.up.util.Ut;

import java.net.ConnectException;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

final class Zero {
    private static final Annal LOGGER = Annal.get(Zero.class);

    private Zero() {
    }

    static <T> void exec(final Consumer<T> consumer, final T input) {
        if (null != input) {
            consumer.accept(input);
        }
    }

    static void exec(final Actuator actuator, final Object... input) {
        if (Zero.isSatisfy(input)) {
            actuator.execute();
        }
    }

    static void execZero(final ZeroActuator actuator, final Object... input)
            throws ZeroException {
        if (Zero.isSatisfy(input)) {
            actuator.execute();
        }
    }

    static <T> T getJvm(final T defaultValue, final JvmSupplier<T> supplier, final Object... input) {
        T ret = null;
        try {
            if (Arrays.stream(input).allMatch(Objects::nonNull)) {
                ret = supplier.get();
            }
        } catch (final ZeroException ex) {
            Zero.LOGGER.zero(ex);
            // TODO: Debug Trace for JVM
            ex.printStackTrace();
        } catch (final ZeroRunException ex) {
            throw ex;
        } catch (final Throwable ex) {
            /*
             * ConnectException will be reach out, it should be checked exception
             * Such as
             * 1) Network timeout
             * 2) Database connected timeout
             * Others here.
             */
            if (!(ex instanceof ConnectException)) {
                Zero.LOGGER.jvm(ex);
            }
            if (!(ex instanceof DateTimeParseException)) {
                // TODO: Debug Trace for JVM
                if (Debugger.onStackTrace()) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (null == ret) {
                ret = defaultValue;
            }
        }
        return ret;
    }

    static <T> T get(final T defaultValue, final Supplier<T> fnGet, final Object... reference) {
        if (Arrays.stream(reference).allMatch(Objects::nonNull)) {
            final T ret = fnGet.get();
            return (null == ret) ? defaultValue : ret;
        } else {
            return defaultValue;
        }
    }

    static <T> T getEmpty(final T defaultValue, final Supplier<T> fnGet, final String... reference) {
        if (Arrays.stream(reference).allMatch(Ut::notNil)) {
            final T ret = fnGet.get();
            return (null == ret) ? defaultValue : ret;
        } else {
            return defaultValue;
        }
    }

    private static boolean isSatisfy(final Object... input) {
        return 0 == input.length || Arrays.stream(input).allMatch(Objects::nonNull);
    }
}
