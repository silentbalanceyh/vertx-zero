package io.horizon.uca.convert;

import io.horizon.annotations.Memory;
import io.horizon.uca.cache.Cc;

/*
 * Vto date here
 */
public interface Vto<T> {

    T to(Object value, Class<?> type);
}

@SuppressWarnings("all")
interface CACHE {
    /**
     * Vto 转换器专用缓存池
     */
    @Memory(Vto.class)
    Cc<String, Vto> CCT_VTO = Cc.openThread();
}