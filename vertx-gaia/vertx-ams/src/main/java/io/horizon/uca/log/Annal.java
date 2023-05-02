package io.horizon.uca.log;

import io.horizon.annotations.Memory;
import io.horizon.specification.uca.HLogger;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.internal.BridgeAnnal;
import io.horizon.uca.log.internal.Log4JAnnal;

import java.util.Objects;

/**
 * Unite Logging system connect to vert.x, io.vertx.zero.io.vertx.zero.io.vertx.up.io.vertx.up.io.vertx.up.util kit of Vertx-Zero
 */
public interface Annal extends HLogger {

    static Annal get(final Class<?> clazz) {
        final Class<?> cacheKey = Objects.isNull(clazz) ? Log4JAnnal.class : clazz;
        return CACHE.CC_ANNAL_EXTENSION.pick(() -> new BridgeAnnal(clazz), cacheKey);
    }
}

interface CACHE {

    /**
     * 按类分配的日志缓存池
     * 内部使用的按 hasCode 分配的日志缓存池
     */
    @Memory(Annal.class)
    Cc<Class<?>, Annal> CC_ANNAL_EXTENSION = Cc.open();
    @Memory(Annal.class)
    Cc<Integer, Annal> CC_ANNAL_INTERNAL = Cc.open();
}