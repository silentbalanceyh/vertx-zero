package io.vertx.up.log.internal;

import io.horizon.exception.ZeroException;
import io.horizon.exception.ZeroRunException;
import io.vertx.up.log.Annal;
import io.vertx.up.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Log4JAnnal implements Annal {

    private transient final Logger logger;

    public Log4JAnnal(final Class<?> clazz) {
        /*
         * Common usage
         */
        this.logger = LoggerFactory.getLogger(clazz);
    }

    @Override
    public void checked(final ZeroException ex) {
        Log.checked(this.logger, ex);
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
    public void runtime(final ZeroRunException ex) {
        Log.runtime(this.logger, ex);
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
    public void info(final boolean condition, final String key, final Object... args) {
        if (condition) {
            Log.info(this.logger, key, args);
        }
    }

    @Override
    public void debug(final String key, final Object... args) {
        Log.debug(this.logger, key, args);
    }
}
