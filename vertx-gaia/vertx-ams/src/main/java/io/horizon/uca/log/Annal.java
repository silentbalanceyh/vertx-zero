package io.horizon.uca.log;

import io.horizon.annotations.Memory;
import io.horizon.specification.uca.HLogger;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.internal.BridgeAnnal;

/**
 * Unite Logging system connect to vert.x, io.vertx.zero.io.vertx.zero.io.vertx.up.io.vertx.up.io.vertx.up.util kit of Vertx-Zero
 */
public interface Annal extends HLogger {

    static Annal get(final Class<?> clazz) {
        /*
         * 旧代码：
         * final Class<?> cacheKey = Objects.isNull(clazz) ? Log4JAnnal.class : clazz;
         * return CACHE.CC_ANNAL_EXTENSION.pick(() -> new BridgeAnnal(clazz), cacheKey);
         * 此处会引起  java.lang.IllegalStateException: Recursive update 的问题
         * 假设底层已提供实现 Logger logger = LoggerFactory.getLogger，所以就不考虑线程安全
         * 问题，现阶段获取日志器会引起很大的线程安全问题，此处不能如此执行，特别针对全局模式下的操作尤其需要小心
         */
        return new BridgeAnnal(clazz);
    }
}

interface CACHE {
    /**
     * 按类分配的日志缓存池
     * 内部使用的按 hasCode 分配的日志缓存池
     * 旧代码：
     * <pre><code>
     * @Memory(Annal.class)
     * Cc<Class < ?>, Annal> CC_ANNAL_EXTENSION = Cc.open();
     * </code></pre>
     */
    @Memory(Annal.class)
    Cc<Integer, Annal> CC_ANNAL_INTERNAL = Cc.open();
}