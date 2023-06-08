package io.horizon.specification.app;

/**
 * 「网络标识」Uri
 * <hr/>
 * 和抽象路径对接实现网络访问，二者配合操作执行
 *
 * @author lang : 2023-05-21
 */
public interface HUri {
    /**
     * 协议
     *
     * @return {@link String}
     */
    String protocol();

    /**
     * IP地址
     *
     * @return {@link String}
     */
    String ip();

    /**
     * 域名
     *
     * @return {@link String}
     */
    String domain();

    /**
     * 转换成 URN 时的操作
     *
     * @return {@link String}
     */
    String urn();

    /**
     * 最终构造的网络标识
     *
     * @return {@link String}
     */
    String uri();
}
