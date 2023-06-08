package io.macrocosm.specification.program;

import io.horizon.specification.typed.TPort;

/**
 * 「持续在线」
 * 观察者：星云，监控端专用，用于描述和监控容器相关的监控端相关数据和上层应用区域的强引用关系
 *
 * <pre><code>
 *     1. 所属关联直接使用 {@link HCube}
 *     2. 监控端所有驾驶舱集合（反向引用）
 * </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HNebula extends TPort {
    /**
     * 直接对应到 {@link HCube} 引用，所属应用区域
     *
     * @return {@link HCube}
     */
    default HCube cube() {
        return null;
    }
}
