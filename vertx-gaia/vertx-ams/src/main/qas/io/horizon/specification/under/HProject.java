package io.horizon.specification.under;

import io.horizon.annotations.reference.One2One;
import io.horizon.specification.secure.HOwner;
import io.horizon.specification.unit.HEditor;
import io.horizon.specification.unit.HLibrary;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 「项目引用」
 * <hr/>
 * 研发中心用户端的核心引用实体（项目），此项目可用于工具类处理项目的核心信息：
 * <pre><code>
 *     1. 多个工程文件
 *     2. 多个工作空间
 *     3. 多个设计器
 * </code></pre>
 *
 * 工程文件和工作空间在边界属性上实现绑定，如
 * <pre><code>
 *     1. 如一个工程文件名为：.xproject | .jproject
 *     2. 而工作空间中会引用工程文件所在的资源目录地址，对接下层的 HStore
 * </code></pre>
 *
 * @author lang : 2023-05-20
 */
public interface HProject {
    /**
     * 当前项目所属的超级账号，一个超级账号之下会包含多个项目，不同的项目可以对应不同的
     * realm 部分，此处的 realm 是从 HOwner 中计算而来。
     *
     * @return {@link HOwner}
     */
    HOwner owner();

    /**
     * 项目关联的库信息
     *
     * @return {@link HRepo}
     */
    @One2One(interaction = true)
    HRepo repo();

    /**
     * 项目关联的抽象资源目录
     *
     * @return 抽象资源引用
     */
    @One2One
    HResource resource();

    /**
     * 工作空间引用的所有依赖库，直接将 HRAD 中的内容引用并执行
     * <pre><code>
     *     1. 添加
     *     2. 删除
     *     3. 更新版本
     * </code></pre>
     *
     * @return {@link Set<HLibrary>}
     */
    Set<HLibrary> dependencies();

    /**
     * 当前项目使用的工具集
     *
     * @return {@link HEditor}
     */
    Set<HEditor> editors();

    /**
     * 工程部署文件映射集
     *
     * @return 返回工程部署文件
     */
    ConcurrentMap<String, Set<String>> descriptors();
}
