package io.macrocosm.specification.app;

import io.horizon.specification.typed.TContract;
import io.macrocosm.specification.program.HNebula;
import io.macrocosm.specification.program.HNovae;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 「应用实例」Backend
 * <hr/>
 * 应用实例用于描述双端核心应用
 * <pre><code>
 *     1. 监控端 {@link io.macrocosm.specification.program.HNebula}
 *     2. 管理端 {@link io.macrocosm.specification.program.HNovae}
 * </code></pre>
 * 运行实例可直接使用反向应用专用处理
 *
 * @author lang : 2023-05-21
 */
public interface HBackend extends TContract {
    /**
     * 所属管理端
     *
     * @return {@link HNovae}
     */
    HNovae manager();

    /**
     * 所属监控端
     *
     * @return {@link HNebula}
     */
    HNebula monitor();


    /**
     * 工程部署文件映射集
     *
     * @return 返回工程部署文件
     */
    ConcurrentMap<String, Set<String>> descriptors();
}
