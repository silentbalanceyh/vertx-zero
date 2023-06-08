package io.macrocosm.specification.program;

import io.horizon.annotations.reference.One2One;
import io.horizon.specification.storage.HStore;
import io.horizon.specification.typed.TPort;
import io.horizon.specification.under.RBranch;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 「应用容器」
 * 远程分支可以发布到应用容器中，应用容器中的应用可以被部署到不同的服务器上，应用容器中的应用可以被部署到不同的服务器上，
 * 所以应用容器必须包含所属的内置容器信息，内置容器还包括 CRI 容器运行时和 K8S 相关的 HPod 空间。
 * <pre><code>
 *     1. 一个应用的起点从 {@link RBranch} 分支开始，分支中包含了开发端的所有信息。
 *     2. 应用运行的起点则是从 {@link HCube} 开始，它包含了如下功能：
 *        - （前）管理端基础资源 {@link HNovae}
 *        - （前）监控端基础资源 {@link HNebula}
 *        - （前）运行端基础资源 {@link HNova}
 *     3. 对接云资源时会关联两部分：
 *        - （云）物理区域资源 {@link HPlot}
 *           - 底层关联到抽象资源空间 {@link io.horizon.specification.under.HResource}
 *           - 物理层关联到抽象物理区域 {@link HStore}
 *        - （云）云容器空间，K8S连接 {@link HPod}
 *     4. 配合发布计划可直接处理三种不同环境中的相关信息
 * </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HCube {
    /**
     * 发布专用的分支信息（分支中会包含库信息）
     *
     * @return {@link RBranch}
     */
    @One2One
    RBranch branch();

    /**
     * 应用容器中的统一标识（唯一标识）
     * <pre><code>
     *     - （弱）下层区域由于对接存储，使用唯一标识符关联
     *     - （强）上层区域直接引用
     * </code></pre>
     *
     * @return {@link String}
     */
    String identifier();

    /**
     * 保存了整个应用区域中所有 {@link TPort} 的引用
     * <pre><code>
     *     - name = instance 1
     *            = instance 2
     *            = instance 3
     * </code></pre>
     * 用于描述五种不同的容器端
     *
     * @return {@link ConcurrentMap}
     */
    ConcurrentMap<Class<?>, Set<TPort>> port();
}
