package io.macrocosm.specification.program;

import io.horizon.specification.typed.TEvent;
import io.horizon.specification.typed.TPort;
import io.macrocosm.atom.boot.KRepo;
import io.macrocosm.eon.em.EmCloud;

import java.util.concurrent.ConcurrentMap;

/**
 * 「持续在线」
 * 执行者：新星，运行端专用，用于描述和核心容器相关的运行端相关数据和上层的应用区域是强引用关系
 * <pre><code>
 *     1. 所属关联直接使用 {@link HCube}
 *     2. 运行端的所有容器运行时集合（反向引用）
 * </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HNova extends TPort, TEvent<ConcurrentMap<EmCloud.Runtime, KRepo>, Boolean> {
    /**
     * 直接对应到 {@link HCube} 引用，所属应用区域
     *
     * @return {@link HCube}
     */
    default HCube cube() {
        return null;
    }
}
