package io.horizon.uca.log;

import io.horizon.eon.cache.CStore;
import io.horizon.uca.log.internal.BridgeAnnal;
import io.horizon.uca.log.internal.Log4JAnnal;
import org.slf4j.Logger;

import java.util.Objects;

public final class Log {

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
            return CStore.CC_ANNAL_INTERNAL.pick(() -> new Log4JAnnal((Logger) input), input.hashCode());
        } else if (input instanceof Annal) {
            Objects.requireNonNull(input);
            // 确保不重复创建
            return CStore.CC_ANNAL_INTERNAL.pick(() -> new BridgeAnnal((Annal) input), input.hashCode());
        } else {
            // 内部方法自带缓存
            return Annal.get((Class<?>) input);
        }
    }
}
