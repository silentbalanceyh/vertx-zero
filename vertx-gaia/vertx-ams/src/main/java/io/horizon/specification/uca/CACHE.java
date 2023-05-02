package io.horizon.specification.uca;

import io.horizon.annotations.Memory;
import io.horizon.uca.cache.Cc;

/**
 * @author lang : 2023/5/2
 */
interface CACHE {
    /*
     * HFS 抽象缓存
     */
    @Memory(HFS.class)
    Cc<String, HFS> CCT_FS = Cc.openThread();
}
