package io.vertx.up.log;

import io.vertx.core.VertxException;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.fn.Actuator;
import io.vertx.up.log.internal.Log4JAnnal;
import io.vertx.up.runtime.ZeroAmbient;
import io.vertx.up.util.Ut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Unite Logging system connect to vert.x, io.vertx.zero.io.vertx.zero.io.vertx.up.io.vertx.up.io.vertx.up.util kit of Vertx-Zero
 */
public interface Annal {

    static Annal get(final Class<?> clazz) {
        return new CommonAnnal(clazz);
    }

    /*
     * Re-invoked logging for executing, here are logger sure to
     * Avoid Null Pointer exception
     */
    static <T> void sure(final Annal logger, final Actuator actuator) {
        if (null != logger) {
            actuator.execute();
        }
    }

    void vertx(VertxException ex);

    void zero(ZeroException ex);

    void jvm(Throwable ex);

    void warn(String key, Object... args);

    void error(String key, Object... args);

    void info(String key, Object... args);

    void info(boolean condition, String key, Object... args);

    void debug(String key, Object... args);
}

class CommonAnnal implements Annal {

    private static final Logger RECORD = LoggerFactory.getLogger(CommonAnnal.class);
    private static final Set<Class<?>> OUTED = new ConcurrentHashSet<>();

    private transient final Annal logger;

    CommonAnnal(final Class<?> clazz) {
        Class<?> inject = ZeroAmbient.getPlugin("logger");
        if (null == inject) {
            Log.debug(RECORD, Info.INF_INJECT, clazz);
            inject = Log4JAnnal.class;
        }
        if (!OUTED.contains(inject)) {
            Log.debug(RECORD, Info.INF_ANNAL, inject, clazz);
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
    public void vertx(final VertxException ex) {
        this.logger.vertx(ex);
    }

    @Override
    public void zero(final ZeroException ex) {
        this.logger.zero(ex);
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
