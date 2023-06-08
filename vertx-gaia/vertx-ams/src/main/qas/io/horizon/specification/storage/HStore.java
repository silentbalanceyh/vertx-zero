package io.horizon.specification.storage;

import io.horizon.eon.em.EmType;
import io.horizon.specification.typed.TContract;
import io.horizon.specification.typed.TExecutor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 「存储入口」Store
 * <hr/>
 * 对应抽象存储接口，提供存储基础协议，上层协议会关联到两个核心方向:
 * <pre><code>
 *     1. {@link io.horizon.specification.under.HResource} 抽象资源存储
 *     2. {@link io.macrocosm.specification.program.HPlot} 抽象物理存储
 * </code></pre>
 * 存储中会包含
 * <pre><code>
 *     - identifier()
 *     - version()
 * </code></pre>
 *
 * @author lang : 2023-05-21
 */
public interface HStore extends TContract {
    /**
     * 执行器集合，针对不同语义的执行器定义
     *
     * @return {@link ConcurrentMap}
     */
    default ConcurrentMap<String, TExecutor> executor() {
        return new ConcurrentHashMap<>();
    }

    /**
     * 关联存储集合，只包含三种存储类型
     *
     * @return {@link ConcurrentMap}
     */
    default ConcurrentMap<EmType.Store, HStore> store() {
        return new ConcurrentHashMap<>();
    }
}
