package io.vertx.up.log;

import io.vertx.up.uca.cache.Cc;
import io.vertx.up.util.Ut;

import java.util.function.Function;

/**
 * @author lang : 2023/4/25
 */
public class LogFactory {

    private static final Cc<String, LogFactory> CC_LOG_FACTORY = Cc.openThread();
    private static final Cc<String, LogModule> CC_LOG_EXTENSION = Cc.open();

    private final String module;

    private LogFactory(final String module) {
        this.module = module;
    }

    static LogFactory create(final String module) {
        return CC_LOG_FACTORY.pick(() -> new LogFactory(module), module);
    }

    public LogModule program(final String type) {
        return this.extension(type, Ut::flagNBlue);
    }

    public LogModule configure(final String type) {
        return this.extension(type, Ut::flagNGreen);
    }

    public LogModule infix(final String type) {
        return this.extension(type, Ut::flagNCyan);
    }

    public LogModule cloud(final String type) {
        return this.extension(type, Ut::flagBBlue);
    }

    private LogModule extension(final String type, final Function<String, String> colorFn) {
        return CC_LOG_EXTENSION.pick(
            () -> new LogModule(this.module).bind(type).bind(colorFn),
            this.module + "/" + type);
    }
}
