package io.vertx.up.log.internal;

import io.vertx.core.VertxException;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.log.Annal;
import io.vertx.up.log.Log;

public class Log4JAnnal implements Annal {

    private transient final Logger logger;

    public Log4JAnnal(final Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    @Override
    public void zero(final ZeroException ex) {
        Log.zero(this.logger, ex);
    }

    @Override
    public void warn(final String key, final Object... args) {
        Log.warn(this.logger, key, args);
    }

    @Override
    public void error(final String key, final Object... args) {
        Log.error(this.logger, key, args);
    }

    @Override
    public void vertx(final VertxException ex) {
        Log.vertx(this.logger, ex);
    }

    @Override
    public void jvm(final Throwable ex) {
        Log.jvm(this.logger, ex);
    }

    @Override
    public void info(final String key, final Object... args) {
        Log.info(this.logger, key, args);
    }

    @Override
    public void debug(final String key, final Object... args) {
        Log.debug(this.logger, key, args);
    }
}
