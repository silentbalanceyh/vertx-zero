package io.mature.extension.scaffold.component;

import io.vertx.up.annotations.Contract;
import io.vertx.up.commune.config.Integration;

/**
 * ## 「Connector」顶层连接器
 *
 * ### 1. 基本介绍
 *
 * 在适配器{@link AbstractAdaptor}上追加集成配置{@link Integration}。
 *
 * ### 2. 组件功能
 *
 * 提供集成实例，对应`I_SERVICE`表中的`configIntegration`属性设置集成配置{@link Integration}。
 *
 * > 集成配置中包含了`debug`属性用于模拟数据接口。
 *
 * ### 3. 配置数据结构
 *
 * #### 3.1. 集成配置结构
 *
 * ```json
 * // <pre><code class="json">
 * {
 *      "endpoint": "集成专用EndPoint端",
 *      "port": "Number, 集成端口",
 *      "username": "集成账号信息",
 *      "password": "账号登陆口令",
 *      "hostname": "集成用IP地址或域名",
 *      "publicKey": "公钥",
 *      "privateKey": "私钥",
 *      "apis":{
 *          "API键1": {
 *              "method": "HTTP方法，不设置则GET",
 *              "uri": "URI地址1"
 *          },
 *          "API键2": {
 *              "method": "HTTP方法，不设置则GET",
 *              "uri": "URI地址2"
 *          }
 *      }
 * }
 * // </code></pre>
 * ```
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractConnector extends AbstractAdaptor {
    /**
     * 「合约」成员实例，`I_SERVICE`表中的`configIntegration`属性设置，关联{@link Integration}对象。
     *
     * 该实例是通过{@link Contract}注解赋值，配置结构如上述文档中所描述，下边是其中一个示例：
     *
     * ```json
     * // <pre><code class="json">
     * {
     *      "endpoint": "http://www.demo.cn/ws/api/",
     *      "port": 1234,
     *      "username": "lang",
     *      "password": "xxxx",
     *      "hostname": "www.demo.cn or 192.168.0.12",
     *      "publicKey": "xxx",
     *      "privateKey": "xxx",
     *      "apis":{
     *          "get.username": {
     *              "method": "POST",
     *              "uri": "/uri/getinfo"
     *          },
     *          "post.test": {
     *              "method": "GET",
     *              "uri": "/uri/getinfo"
     *          }
     *      }
     * }
     * // </code></pre>
     * ```
     */
    @Contract
    private transient Integration integration;

    /**
     * 返回当前系统中的集成配置引用实例。
     *
     * @return {@link Integration}集成实例
     */
    protected Integration integration() {
        return this.integration;
    }
}
