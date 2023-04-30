package io.horizon.eon.cache;

import io.horizon.uca.cache.Cc;
import io.horizon.uca.convert.Vto;
import io.horizon.uca.log.Annal;

/**
 * @author lang : 2023/4/30
 */
@SuppressWarnings("all")
interface CUCA {
    /**
     * Vto 转换器专用缓存池
     */
    Cc<String, Vto> CCT_VTO = Cc.openThread();

    /**
     * 按类分配的日志缓存池
     * 内部使用的按 hasCode 分配的日志缓存池
     */
    Cc<Class<?>, Annal> CC_ANNAL_EXTENSION = Cc.open();
    Cc<Integer, Annal> CC_ANNAL_INTERNAL = Cc.open();
}
