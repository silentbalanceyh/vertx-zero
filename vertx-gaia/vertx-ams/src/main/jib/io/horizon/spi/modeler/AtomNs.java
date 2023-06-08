package io.horizon.spi.modeler;

/**
 * 针对模型所属名空间的专用SPI，分成两级操作
 * <pre><code>
 *     1. 直接根据应用名称读取应用名空间
 *     2. 直接根据应用名称和模型的 identifier 读取名空间
 * </code></pre>
 *
 * 替换原始的 `ns` 静态方法调用，改用 SPI 方式实现。
 *
 * @author lang : 2023-05-08
 */
public interface AtomNs {

    /**
     * 根据应用程序名称读取该应用名称的名空间信息
     *
     * @param appName 应用名称
     *
     * @return 应用名空间
     */
    String ns(String appName);

    /**
     * 根据应用程序名称和模型的统一标识符 identifier 读取该应用名称的名空间信息
     *
     * @param appName    应用名称
     * @param identifier 模型统一标识符
     *
     * @return 应用名空间
     */
    String ns(String appName, String identifier);
}
