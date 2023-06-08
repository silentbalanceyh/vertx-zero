package io.horizon.specification.typed;

import io.horizon.eon.em.EmUca;
import io.macrocosm.specification.app.HModule;

/**
 * 「组件契约」Component
 * 位于 {@link HModule} 之下的运行组件：
 * <pre><code>
 *     1. 核心运行组件（功能性、非功能性）
 *     2. 第三方插件
 *     3. 扩展插件
 * </code></pre>
 * 针对运行端（Runtime）级的所有组件信息 / 实例和容器互相处理
 *
 * @author lang : 2023-05-21
 */
public interface TComponent extends TContract {
    /**
     * 组件的名称，用于描述组件的名称
     *
     * @return {@link String}
     */
    String name();

    /**
     * 当前组件的状态
     *
     * @return {@link EmUca.Status}
     */
    EmUca.Status status();
}
