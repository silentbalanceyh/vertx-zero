package io.horizon.specification.typed;

import io.horizon.eon.em.EmUca;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 「端契约」Port
 * <hr/>
 * 端契约用于处理云连接端专用，会比普通契约多一个 uri 对应的地址用于做网络层标识
 * <pre><code>
 *     带界面部分
 *     - 管理端：{@link io.macrocosm.specification.program.HNovae}
 *     - 监控端：{@link io.macrocosm.specification.program.HNebula}
 *     - 运行端：{@link io.macrocosm.specification.program.HNova}
 *     不带界面部分
 *     - 容器端：{@link io.macrocosm.specification.program.HPod}
 *     - 物理端：{@link io.macrocosm.specification.program.HPlot}
 * </code></pre>
 *
 * @author lang : 2023-05-21
 */
public interface TPort extends TContract {
    /**
     * 端的入口地址，该地址主要应用于前端界面的访问
     *
     * @return {@link String}
     */
    default String entry() {
        return null;
    }

    /**
     * URI 标识，用于标识端的网络地址
     *
     * @return {@link String}
     */
    default String uri() {
        return null;
    }

    /**
     * 当前端中正在运行的应用实例 identifier，哈希表中的值
     * 还记录了每个实例的运行状态，用来管理实例相关的基础生命周期
     *
     * @return {@link ConcurrentMap}
     */
    default ConcurrentMap<String, EmUca.Status> running() {
        return new ConcurrentHashMap<>();
    }
}
