package io.horizon.specification.app;

import io.horizon.eon.VString;

/**
 * 「版本」Version
 * <hr/>
 * 版本信息，用于描述当前应用的版本信息，包含了版本号、版本名称、版本描述、版本发布时间等信息
 * 对应的版本包括：
 * <pre><code>
 *     1. verMajor：主版本号
 *     2. verSub：次版本号
 *     3. verBuild：构建版本号
 *     4. verFull：全版本号
 * </code></pre>
 * 版本部分在发布时使用，用于管理，开发过程中的版本管理没有任何意义
 *
 * @author lang : 2023-05-20
 */
public interface HVersion {
    /**
     * 全版本号
     *
     * @return {@link String}
     */
    default String verFull() {
        return this.verMajor() + VString.DOT
            + this.verSub() + VString.DOT
            + this.verBuild();
    }

    /**
     * 主版本
     *
     * @return {@link String}
     */
    String verMajor();

    /**
     * 次版本
     *
     * @return {@link String}
     */
    String verSub();

    /**
     * 构建版本号
     *
     * @return {@link String}
     */
    String verBuild();
}
