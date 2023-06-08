package io.macrocosm.specification.program;

import io.horizon.specification.typed.TPort;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 「物理区域」Plot
 * 对应到底层的基础软件空间，直接和底层物理设备中对接，容器中会包含一个所属，用来
 * 标识当前 Plot 的边界区域，物理区域和应用区域也是多对一的处理
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HPlot extends TPort {
    /**
     * 直接对应到 {@link HCube} 中的 identifier 信息，执行软关联
     *
     * @return {@link String}
     */
    String reference();

    /**
     * 弱引用，上层的应用信息，描述当前物理区域中运行了哪些应用容器
     *
     * @return {@link ConcurrentMap}
     */
    ConcurrentMap<String, Set<String>> pod();


    /**
     * 当前资源关联的所有存储信息，可主动提取存储信息
     *
     * @return {@link Set<String>}
     */
    Set<String> store();
}
