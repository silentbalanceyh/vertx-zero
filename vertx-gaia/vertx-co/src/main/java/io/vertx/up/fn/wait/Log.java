package io.vertx.up.fn.wait;

import io.vertx.up.eon.Strings;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class Log {

    private static final ConcurrentMap<Integer, Annal> LOGGERS
        = new ConcurrentHashMap<>();
    private static Log INSTANCE;
    private final transient Annal logger;
    private transient String key;

    private Log(final Class<?> clazz) {
        this.logger = Fn.pool(LOGGERS, clazz.hashCode(), () -> Annal.get(clazz));
    }

    public static Log create(final Class<?> clazz) {
        if (null == INSTANCE) {
            INSTANCE = new Log(clazz);
        }
        return INSTANCE;
    }

    public Log on(final String key) {
        this.key = key;
        return this;
    }

    public Log info(final Object... args) {
        // Ready to output.
        if (Ut.isNil(this.key)) {
            final StringBuilder pattern = new StringBuilder();
            for (int idx = 0; idx < args.length; idx++) {
                pattern.append(Strings.LEFT_BRACES)
                    .append(idx)
                    .append(Strings.RIGHT_BRACES)
                    .append(" ");
            }
            this.logger.info(pattern.toString(), args);
        } else {
            this.logger.info(this.key, args);
        }
        // Clear
        this.key = null;
        return this;
    }
}
