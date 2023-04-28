package io.horizon.uca.log;

import io.horizon.uca.cache.Cc;
import io.horizon.util.HaS;

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
        return this.extension(type, HaS::rgbBlueN);
    }

    public LogModule configure(final String type) {
        return this.extension(type, HaS::rgbGreenN);
    }

    public LogModule infix(final String type) {
        return this.extension(type, HaS::rgbCyanN);
    }

    public LogModule cloud(final String type) {
        return this.extension(type, HaS::rgbBlueB);
    }

    private LogModule extension(final String type, final Function<String, String> colorFn) {
        return CC_LOG_EXTENSION.pick(
            () -> new LogModule(this.module).bind(type).bind(colorFn),
            this.module + "/" + type);
    }
}
