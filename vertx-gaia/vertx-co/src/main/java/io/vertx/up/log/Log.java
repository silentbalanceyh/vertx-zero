package io.vertx.up.log;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxException;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.fn.Fn;
import org.slf4j.Logger;

import java.text.MessageFormat;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class Log {
    /*
     * The color for console output
     */
    public static final int COLOR_RED = 31;
    public static final int COLOR_GREEN = 32;
    public static final int COLOR_YELLOW = 33;
    public static final int COLOR_BLUE = 34;
    public static final int COLOR_CYAN = 36;
    public static final int COLOR_GRAY = 37;
    public static final int COLOR_BLANK = 30;
    private static final int NORMAL = 0;
    private static final int WEIGHT = 1;
    private static final String PREFIX = "\u001b[";
    private static final String SUFFIX = "m";
    private static final char SEPARATOR = ';';
    private static final String END_COLOUR = PREFIX + SUFFIX;
    private static final String BOLD_FLAG = PREFIX + WEIGHT + SEPARATOR + COLOR_BLANK + SUFFIX + "[ μηδέν ] " + END_COLOUR;

    public static String blue(final String flag) {
        return PREFIX + NORMAL + SEPARATOR + COLOR_BLUE + SUFFIX + "[ " + flag + " ] " + END_COLOUR;
    }

    public static String green(final String flag) {
        return PREFIX + NORMAL + SEPARATOR + COLOR_GREEN + SUFFIX + "[ " + flag + " ] " + END_COLOUR;
    }

    public static String color(final String flag, final int color) {
        return PREFIX + NORMAL + SEPARATOR + color + SUFFIX + flag + END_COLOUR;
    }

    public static String color(final String flag, final int color, final boolean bold) {
        final int weight = bold ? WEIGHT : NORMAL;
        return PREFIX + weight + SEPARATOR + color + SUFFIX + flag + END_COLOUR;
    }

    public static void jvm(final Logger logger, final Throwable ex) {
        Fn.safeNull(error -> logger.warn("", error), ex);
        if (Debugger.onStackTracing()) {
            /* Default to false */
            ex.printStackTrace();
        }
    }

    public static void zero(final Logger logger, final ZeroException ex) {
        Fn.safeNull(error -> logger.warn("", error), ex);
    }

    public static void vertx(final Logger logger, final VertxException ex) {
        Fn.safeNull(error -> logger.warn("", error), ex);
    }

    public static void info(final Logger logger, final String pattern, final Object... rest) {
        log(logger::isInfoEnabled, logger::info, pattern, rest);
    }

    public static void debug(final Logger logger, final String pattern, final Object... rest) {
        log(() -> true, logger::debug, pattern, rest);
    }

    public static void warn(final Logger logger, final String pattern, final Object... rest) {
        log(() -> true, logger::warn, pattern, rest);
    }

    public static void error(final Logger logger, final String pattern, final Object... rest) {
        log(() -> true, logger::error, pattern, rest);
    }

    private static void log(final Supplier<Boolean> fnPre,
                            final BiConsumer<String, Object> fnLog,
                            final String message,
                            final Object... rest) {
        if (fnPre.get()) {
            final String formatted;
            if (0 < rest.length) {
                formatted = BOLD_FLAG + " " + MessageFormat.format(message, rest);
            } else {
                formatted = BOLD_FLAG + " " + message;
            }
            fnLog.accept(formatted, null);
        }
    }

    public static class Health {
        private final transient Vertx vertx;

        private Health(final Vertx vertx) {
            this.vertx = vertx;
        }

        public static Health on(final Vertx vertx) {
            return new Health(vertx);
        }

        public void add(final Class<?> clazz, final DeploymentOptions options, final String id) {
            Meansure.add(this.vertx, clazz.getName(), options, id);
        }

        public void add(final String name, final DeploymentOptions options, final String id) {
            Meansure.add(this.vertx, name, options, id);
        }

        public void remove(final Class<?> clazz, final DeploymentOptions options) {
            Meansure.remove(this.vertx, clazz.getName(), options);
        }
    }
}
