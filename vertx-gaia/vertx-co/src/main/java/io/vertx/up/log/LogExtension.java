package io.vertx.up.log;

import io.vertx.up.uca.cache.Cc;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author lang : 2023/4/25
 */
public class LogExtension {
    private static final Cc<String, LogExtension> CC_LOG_EXTENSION = Cc.openThread();
    private final String module;
    private String type;
    private Function<String, String> colorFn;

    LogExtension(final String module) {
        this.module = module;
    }

    public static LogExtension instance(final String module) {
        return CC_LOG_EXTENSION.pick(() -> new LogExtension(module), module);
    }

    public LogExtension bind(final Function<String, String> colorFn) {
        this.colorFn = colorFn;
        return this;
    }

    /*
     * DEFAULT ->
     * type -> module / type
     */
    public LogExtension bind(final String type) {
        synchronized (this) {
            this.type = type;
        }
        return this;
    }

    private String format(final String pattern) {
        return " [ " + (Objects.isNull(this.colorFn) ? this.module : this.colorFn.apply(this.module)) + " ] "
            + " ( " + this.type + " ) " + pattern;
    }

    public void info(final Class<?> clazz, final String pattern, final Object... args) {
        Annal.get(clazz).info(this.format(pattern), args);
    }

    public void debug(final Class<?> clazz, final String pattern, final Object... args) {
        Annal.get(clazz).debug(this.format(pattern), args);
    }

    public void warn(final Class<?> clazz, final String pattern, final Object... args) {
        Annal.get(clazz).warn(this.format(pattern), args);
    }

    public void error(final Class<?> clazz, final String pattern, final Object... args) {
        Annal.get(clazz).error(this.format(pattern), args);
    }

    public void debug(final Annal logger, final String pattern, final Object... args) {
        final Annal annal = Log.logger(logger);
        annal.debug(this.format(pattern), args);
    }

    public void warn(final Annal logger, final String pattern, final Object... args) {
        final Annal annal = Log.logger(logger);
        annal.warn(this.format(pattern), args);
    }

    public void error(final Annal logger, final String pattern, final Object... args) {
        final Annal annal = Log.logger(logger);
        annal.error(this.format(pattern), args);
    }
}
