package io.horizon.runtime.cache;

import io.horizon.annotations.Memory;
import io.horizon.specification.storage.HFS;
import io.horizon.uca.cache.Cc;

/**
 * 包内专用抽象组件缓存
 *
 * @author lang : 2023/5/2
 */
interface CUca {
    /*
     * HFS 抽象缓存
     */
    @Memory(HFS.class)
    Cc<String, HFS> CCT_FS = Cc.openThread();
}
