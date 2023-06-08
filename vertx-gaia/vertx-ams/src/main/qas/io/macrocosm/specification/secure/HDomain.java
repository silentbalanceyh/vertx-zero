package io.macrocosm.specification.secure;

import io.horizon.annotations.reference.One2One;
import io.horizon.specification.app.HBoundary;

/**
 * 「应用标准用户」
 * <hr/>
 * 应用边界之下的顶层账号，一个应用边界中可以包含多个应用标准账号，应用账号是为账号发布应用量身打造的
 * 账号系统，所有云端账号可保留三个级别应用发布:
 *
 * <pre><code>
 *     1. 云端系统（多平台模式）
 *     2. 平台系统（多租户模式）
 *     3. 单点应用系统（SaaS模式）{@link HDomain}
 * </code></pre>
 *
 * @author lang : 2023-05-20
 */
public interface HDomain extends HBoundary<HSpace> {
    /**
     * 返回当前账号容器绑定的应用租户边界，使用强类型，在引用过程中可直接处理
     *
     * @return {@link HSpace}
     */
    @Override
    @One2One
    HSpace realm();

    /**
     * 返回当前标准应用下的所有账号，账号信息本身支持树型结构
     *
     * @return {@link HAccount}
     */
    @One2One
    HAccount account();
}
