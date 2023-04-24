package io.vertx.up.log.internal;

import io.horizon.eon.info.VLog;
import io.horizon.exception.ZeroException;
import io.horizon.exception.ZeroRunException;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.up.log.Annal;
import io.vertx.up.log.Log;
import io.vertx.up.runtime.ZeroAmbient;
import io.vertx.up.util.Ut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @author lang : 2023/4/25
 */
public class BridgeAnnal implements Annal {
    /*
     * LoggerFactory.getLogger
     * Default implementation for Logger here, the logger of zero framework is
     * logback instead of log4j / log4j2, this is common usage.
     */
    private static final Logger RECORD = LoggerFactory.getLogger(BridgeAnnal.class);
    private static final Set<Class<?>> OUTED = new ConcurrentHashSet<>();

    private transient final Annal logger;

    public BridgeAnnal(final Class<?> clazz) {
        Class<?> inject = ZeroAmbient.getPlugin("logger");
        if (null == inject) {
            Log.debug(RECORD, VLog.ANNAL_INTERNAL, clazz);
            inject = Log4JAnnal.class;
        }
        if (!OUTED.contains(inject)) {
            Log.debug(RECORD, VLog.ANNAL_CONFIGURED, inject, clazz);
            OUTED.add(inject);
        }
        this.logger = Ut.instance(inject, clazz);
    }

    @Override
    public void warn(final String key, final Object... args) {
        this.logger.warn(key, args);
    }

    @Override
    public void error(final String key, final Object... args) {
        this.logger.error(key, args);
    }

    @Override
    public void runtime(final ZeroRunException ex) {
        this.logger.runtime(ex);
    }

    @Override
    public void checked(final ZeroException ex) {
        this.logger.checked(ex);
    }

    @Override
    public void jvm(final Throwable ex) {
        this.logger.jvm(ex);
    }

    @Override
    public void info(final String key, final Object... args) {
        this.logger.info(key, args);
    }

    @Override
    public void info(final boolean condition, final String key, final Object... args) {
        if (condition) {
            this.info(key, args);
        }
    }

    @Override
    public void debug(final String key, final Object... args) {
        this.logger.debug(key, args);
    }
}
