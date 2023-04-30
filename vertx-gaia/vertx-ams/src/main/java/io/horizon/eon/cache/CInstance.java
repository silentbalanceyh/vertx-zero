package io.horizon.eon.cache;

import io.horizon.uca.cache.Cc;

/**
 * @author lang : 2023/4/30
 */
interface CInstance {

    /**
     * 全局单件模式专用
     */
    Cc<String, Object> CC_SINGLETON = Cc.open();
    /**
     * 全局类缓存专用
     */
    Cc<String, Class<?>> CC_CLASSES = Cc.open();
}
