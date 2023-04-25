package io.vertx.up.log;

import io.vertx.up.log.internal.BridgeAnnal;
import io.vertx.up.log.internal.Log4JAnnal;
import io.vertx.up.uca.cache.Cc;

import java.util.Objects;

/**
 * Unite Logging system connect to vert.x, io.vertx.zero.io.vertx.zero.io.vertx.up.io.vertx.up.io.vertx.up.util kit of Vertx-Zero
 */
public interface Annal {

    Cc<Class<?>, Annal> CC_ANNAL = Cc.open();
    Cc<Integer, Annal> CC_ANNAL_INTERNAL = Cc.open();

    static Annal get(final Class<?> clazz) {
        final Class<?> cacheKey = Objects.isNull(clazz) ? Log4JAnnal.class : clazz;
        return CC_ANNAL.pick(() -> new BridgeAnnal(clazz), cacheKey);
    }


    // -------------- 异常专用日志方法

    void fatal(Throwable ex);

    // -------------- 通用日志方法
    void warn(String key, Object... args);

    void error(String key, Object... args);

    void info(String key, Object... args);

    void info(boolean condition, String key, Object... args);

    void debug(String key, Object... args);
}
