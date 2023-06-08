package io.horizon.specification.unit;

/**
 * 「研发」库信息
 * <hr/>
 *
 * 库信息用于描述当前环境中RAD中包含的所有系统级的库相关信息：
 * <pre><code>
 *     1. identifier：库的唯一标识
 *     2. group: 库的组信息
 *     3. scope：库的作用域
 *     4. version：库的版本记录
 *     5. uri：存储库文件的网络地址
 * </code></pre>
 * 若是本地地址，则可以使用本地的 FILE 协议模式
 *
 * @author lang : 2023-05-20
 */
public interface HLibrary {
    /**
     * 库的唯一标识
     *
     * @return {@link String}
     */
    String identifier();

    /**
     * 库的组信息
     *
     * @return {@link String}
     */
    String group();

    /**
     * 库的作用域
     *
     * @return {@link String}
     */
    String scope();

    /**
     * 库的版本记录
     *
     * @return {@link String}
     */
    String version();

    /**
     * 存储库文件的网络地址
     *
     * @return {@link String}
     */
    String uri();
}
