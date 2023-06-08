package io.macrocosm.specification.secure;

import io.horizon.annotations.reference.One2One;
import io.horizon.specification.app.HBoundary;

/**
 * 「云端标准租户」
 * <hr/>
 * 租户边界之下的顶层账号，一个租户边界中可能包含多个云端标准租户，云端标准租户是为租户发布应用量身打造的
 * 账号系统，所有云端账号可保留三个级别的应用发布：
 *
 * <pre><code>
 *     1. 云端系统（多平台模式），{@link HTenant}
 *     2. 平台系统（多租户模式）
 *     3. 单点应用系统（SaaS模式）
 * </code></pre>
 *
 * 此类与其说是账号不如说是容器系统，它标识了整个账号所具备的基础容器，且内部会包含多个子账号相关信息，实现
 * 完整的账号统一管理。
 *
 * @author lang : 2023-05-20
 */
public interface HTenant extends HBoundary<HFrontier> {
    /**
     * 返回当前账号容器绑定的云租户边界，使用强类型，在引用过程中可直接引用构造好的边界实例
     *
     * @return {@link HFrontier}
     */
    @Override
    @One2One
    HFrontier realm();

    /**
     * 返回当前标准租户旗下的所有账号信息，账号信息本身支持树型结构，
     * 所以此处和账号本身的关联是 1:1 的结构。
     *
     * @return {@link HAccount}
     */
    @One2One
    HAccount account();
}
