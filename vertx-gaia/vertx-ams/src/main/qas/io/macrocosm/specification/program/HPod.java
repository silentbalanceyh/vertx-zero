package io.macrocosm.specification.program;

import io.horizon.specification.typed.TPort;

import java.util.Set;

/**
 * 「云容器空间」Pod
 * 对应到 K8S 的基础软件空间，直接和 K8S 原生容器对接，该容器会包含一个所属，用来标识
 * 当前 Pod 的边界区域，而云容器和应用区域是多对一的处理
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HPod extends TPort {
    /**
     * 直接对应到 {@link HCube} 中的 identifier 信息，执行软关联
     *
     * @return {@link String}
     */
    String reference();

    /**
     * 底层的物理底座，描述当前容器运行在哪些物理区域中，直接对应内部类型
     * {@link HPlot}
     *
     * @return {@link Set}
     */
    Set<HPlot> zone();
}
