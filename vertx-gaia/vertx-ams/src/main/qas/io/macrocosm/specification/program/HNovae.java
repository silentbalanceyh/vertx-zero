package io.macrocosm.specification.program;

import io.horizon.specification.typed.TEvent;
import io.horizon.specification.typed.TPort;
import io.macrocosm.specification.nc.HAeon;

/**
 * 「持续在线」
 * 管理者：新星（复），管理端专用，用于描述和管理端容器相关的相关数据和上层引用的区域强引用关系
 * <pre><code>
 *     1. 所属关联直接使用 {@link HCube}
 *     2. 管理端的所有部署集合和管理端应用（反向引用）
 * </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HNovae extends TPort, TEvent<HAeon, Boolean> {
    /**
     * 直接对应到 {@link HCube} 引用，所属应用区域
     *
     * @return {@link HCube}
     */
    default HCube cube() {
        return null;
    }
}
