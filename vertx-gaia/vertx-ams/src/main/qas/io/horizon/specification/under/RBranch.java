package io.horizon.specification.under;

import io.horizon.annotations.reference.One2One;
import io.horizon.specification.app.HVersion;

/**
 * 「代码分支」Repository Branch
 * <hr/>
 * 代码分支，每个代码分支都会关联到一个代码仓库，而代码仓库中会包含多个代码分支，分支基础规范：
 * <pre><code>
 *     1. 主分支：master        生产发布分支
 *     2. 开发分支：develop     开发分支
 *     3. 测试分支：test        测试分支
 * </code></pre>
 *
 * @author lang : 2023-05-20
 */
public interface RBranch {
    /**
     * 分支所属的库
     *
     * @return {@link HRepo}
     */
    HRepo repo();

    /**
     * 分支名称
     *
     * @return {@link String}
     */
    String name();

    /**
     * 分支专用独立的 URI 地址
     *
     * @return {@link String}
     */
    String uri();

    /**
     * 当前分支的运行时版本，此处需要注意几个事：
     * <pre><code>
     *     1. 分支只有 working() 当前分支才会有版本信息，其他时候不需包含版本信息
     *     2. 分支的版本信息是由分支的运行时版本决定的，而不是由分支的代码版本决定的
     *     3. 版本主要是应用于发布，包括版本库的发布
     * </code></pre>
     *
     * @return {@link HVersion}
     */
    @One2One
    HVersion version();
}
