package io.horizon.specification.typed;

import io.horizon.eon.VValue;
import io.macrocosm.specification.app.HModule;

/**
 * 「契约接口」Contract
 * <hr/>
 * 抽象容器专用接口，包含了容器的基础属性，这些容器会用于描述：
 * <pre><code>
 *     1. identifier()：容器的系统标识（系统级）
 *     2. version()：版本号
 * </code></pre>
 *
 * 此接口用于描述容器类属性，整个系统中的所有组件主要分为三类：
 * <pre><code>
 *     1. 研发中心专用组件，隶属于 {@link io.horizon.specification.under.HRAD}
 *        - （类）研发中心组件为库中组件，实现子类接口 {@link TProducer}
 *     2. 运行端专用组件，关联到 {@link io.macrocosm.specification.program.HCube}
 *        - （实例）运行端组件为实例中组件，实现子类接口 {@link TPort}
 *     3. 模块化插件，关联到 {@link HModule}
 *        - （实例）运行插件中组件，实现子类接口 {@link TComponent}
 * </code></pre>
 *
 * @author lang : 2023-05-21
 */
public interface TContract {
    /**
     * 容器的标识，具有特殊的唯一性
     *
     * @return {@link String}
     */
    default String identifier() {
        return this.getClass().getName();
    }

    /**
     * 容器的版本号
     *
     * @return {@link String}
     */
    default String version() {
        return VValue.DEFAULT_VERSION;
    }
}
