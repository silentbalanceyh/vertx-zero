package io.horizon.specification.under;

import io.horizon.annotations.reference.One2One;
import io.horizon.eon.VValue;
import io.macrocosm.atom.boot.KRepo;

import java.util.Set;

/**
 * 「代码库」Repository
 * <hr/>
 * 代码库用于描述当前环境中 HWorkshop 对应的代码库相关信息，它的关系如：
 * <pre><code>
 *     1. 和 HWorkshop 是 1:N 的关系
 *     2. 和 HProject 是 1:1 的关系
 * </code></pre>
 *
 * @author lang : 2023-05-20
 */
public interface HRepo {
    /**
     * 当前代码库对应的项目
     *
     * @return {@link HProject}
     */
    @One2One(interaction = true)
    HProject project();

    /**
     * 当前代码库对应的工作空间
     *
     * @return {@link KRepo}
     */
    KRepo repository();

    /**
     * 当前代码库对应的工作空间
     *
     * @return {@link HWorkshop}
     */
    HWorkshop workshop();

    /**
     * 返回当前库所有分支信息，默认必须带 master
     *
     * @return {@link Set<String>}
     */
    default Set<String> branches() {
        return Set.of(VValue.DEFAULT_BRANCH_MASTER);
    }
}
