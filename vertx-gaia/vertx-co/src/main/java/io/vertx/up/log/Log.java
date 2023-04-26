package io.vertx.up.log;

import io.vertx.up.log.internal.BridgeAnnal;
import io.vertx.up.log.internal.Log4JAnnal;
import org.slf4j.Logger;

import java.util.Objects;

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

    // -------------- 扩展日志
    public static LogFactory modulat(final String module) {
        return LogFactory.create(module);
    }

    // -------------- 快速日志，比如传入首参 Logger
    /*
     * 三种日志处理
     * 1. Logger 传入 -> Log4JAnnal(Logger)
     * 2. Annal 传入  -> BridgeAnnal(Annal)
     * 4. Class<?> 传入 -> BridgeAnnal(Class<?>) -> Log4JAnnal(Class<?>)
     */
    public static <I> void fatal(final I input, final Throwable ex) {
        final Annal annal = logger(input);
        annal.fatal(ex);
    }

    public static <I> void info(final I input, final String message, final Object... args) {
        final Annal annal = logger(input);
        annal.info(message, args);
    }

    public static <I> void warn(final I input, final String message, final Object... args) {
        final Annal annal = logger(input);
        annal.warn(message, args);
    }

    public static <I> void error(final I input, final String message, final Object... args) {
        final Annal annal = logger(input);
        annal.error(message, args);
    }

    public static <I> void debug(final I input, final String message, final Object... args) {
        final Annal annal = logger(input);
        annal.debug(message, args);
    }

    static <I> Annal logger(final I input) {
        if (input instanceof Logger) {
            Objects.requireNonNull(input);
            // 确保不重复创建
            return Annal.CC_ANNAL_INTERNAL.pick(() -> new Log4JAnnal((Logger) input), input.hashCode());
        } else if (input instanceof Annal) {
            Objects.requireNonNull(input);
            // 确保不重复创建
            return Annal.CC_ANNAL_INTERNAL.pick(() -> new BridgeAnnal((Annal) input), input.hashCode());
        } else {
            // 内部方法自带缓存
            return Annal.get((Class<?>) input);
        }
    }
}
