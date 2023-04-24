package io.horizon.constant.em.cloud;

/**
 * 应用专用维度
 * 原生云模式下，开始有「云租户」和「空间租户」的概念，开容器的条件
 * 1. 维度一：语言容器：             X-Lang / language        （不同语言配置空间不同）
 * 2. 维度二：租户容器：             X-Tenant / tenant        （不同租户配置空间不同）
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum ModeApp {
    /*
     * 「独立模式」
     * 等价维度（现在使用的模式）
     * - sigma
     * - appId
     * 整个Zero运行成一个独立应用，这个应用拥有唯一的应用ID，且这个应用的 sigma 也只有
     * 唯一的一个，sigma 的值也可标识应用，不开容器也可部署。
     *
     * 1 sigma -- x tenant -- 1 appId
     */
    CUBE,

    /*
     * 「多租户模式」（自动多应用）
     * 等价维度
     * - sigma
     * - tenant
     * 此时，不同的租户会开不同的空间，Zero运行成一个平台应用，应用ID独立开容器，一个租户
     * 会包含一个或多个 app
     *
     * 1 sigma -- 1 tenant -- n appId
     */
    SPACE,

    /*
     * 「多层租户模式」（自动多应用）
     * 等价维度（无）
     *
     * 梯度维度
     * - 维度一：sigma，代表统一标识符（云租户）
     * - 维度二：tenant，代表二层租户（空间租户）
     * - 维度三：appId，一个租户会包含一个或多个 app
     *
     * 1 sigma -- n tenant -- n appId
     */
    GALAXY,
}
