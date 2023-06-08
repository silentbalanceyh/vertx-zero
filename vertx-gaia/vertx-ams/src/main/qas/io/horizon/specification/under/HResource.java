package io.horizon.specification.under;

import java.util.Set;

/**
 * 「工作区资源」Resource Directory
 * <hr/>
 * 工作区抽象资源，抽象资源不会直接关联到具体资源，而是用来描述资源的抽象信息，比如资源的类型、名称、描述等
 * 以及资源本身的元数据文件相关内容，而工作区资源的具体内容则是和项目绑定，但会包含所属的工作区信息，三者
 * <pre><code>
 *     1. HWorkshop：工作区和资源是 1:N 的关系
 *     2. HProject：项目和资源是 1:1 的关系，从项目中实现
 *     3. HResource：
 *        -- 工程文件（内部HStore描述）
 *        -- 元数据描述文件（MANIFEST.MF）
 * </code></pre>
 *
 * @author lang : 2023-05-20
 */
public interface HResource {
    /**
     * 该资源空间关联的工作区
     *
     * @return {@link HWorkshop}
     */
    HWorkshop workshop();

    /**
     * 核心元数据描述文件名
     *
     * @return {@link String}
     */
    String manifest();

    /**
     * 工程文件路径（文件名）
     *
     * @return {@link String}
     */
    String path();

    /**
     * 当前资源关联的所有存储信息，可主动提取存储信息
     *
     * @return {@link Set<String>}
     */
    Set<String> store();
}
