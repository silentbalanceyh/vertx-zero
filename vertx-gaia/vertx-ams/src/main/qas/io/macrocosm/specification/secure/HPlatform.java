package io.macrocosm.specification.secure;

import io.horizon.annotations.reference.One2One;
import io.horizon.specification.app.HBoundary;

/**
 * 「平台标准住户」
 * <hr/>
 * 标准住户下的顶层账号区域，一个平台标准租户可能包含多个平台账号，平台账号是平台的最高权限账号。
 * 所有云端账号可保留三个级别的应用发布：
 *
 * <pre><code>
 *     1. 云端系统（多平台模式）
 *     2. 平台系统（多租户模式），{@link HPlatform}
 *     3. 单点应用系统（SaaS模式）
 * </code></pre>
 *
 * @author lang : 2023-05-20
 */
public interface HPlatform extends HBoundary<HGalaxy> {
    /**
     * 返回当前账号容器绑定的平台租户边界，使用强类型，在引用过程中可直接引用构造好的边界实例
     *
     * @return {@link HGalaxy}
     */
    @Override
    @One2One
    HGalaxy realm();

    /**
     * 返回当前标准租户旗下的所有账号信息，账号信息本身支持树型结构，
     * 所以此处和账号本身的关联是 1:1 的结构。
     *
     * @return {@link HAccount}
     */
    @One2One
    HAccount account();
}
