package io.vertx.up.log;

import io.vertx.up.uca.cache.Cc;
import io.vertx.up.util.Ut;

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
        return CC_LOG_EXTENSION.pick(
            () -> new LogModule(this.module).bind(type).bind(Ut::flagNBlue),
            this.module + "/" + type);
    }

    public LogModule configure(final String type) {
        return CC_LOG_EXTENSION.pick(
            () -> new LogModule(this.module).bind(type).bind(Ut::flagNGreen),
            this.module + "/" + type);
    }
}
