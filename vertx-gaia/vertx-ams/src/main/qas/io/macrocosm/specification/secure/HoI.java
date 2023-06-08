package io.macrocosm.specification.secure;

import io.horizon.specification.app.HBelong;
import io.macrocosm.specification.app.HAmbient;

import java.util.function.Function;

/**
 * 原 {@see HOI} 代码部分，区别一下基础概念和对象
 * <pre><code>
 *     > 业务层面的应用概念，这种应用不支持动态建模，包含了基础信息
 *       基本规范
 *       - name:            应用名称
 *       - ns:              应用名空间
 *       扩展规范
 *       - language:        应用使用的语言信息（非浏览器）
 *     > 中层环境基础信息，开放更多系统级字段
 *       - appId:           应用系统主键，对应 X_APP 中的 key
 *       - appKey:          应用系统敏感主键，对应 X_APP 中的 appKey
 *       - code:            应用编码，对应 X_APP 中的 code（保留）
 *     > {@link HoI} 是开放 Aeon 系统之后的系统级信息，包含了不同的租户环境
 *       - 单租户 / 单应用
 *       - 单租户 / 多应用
 *       - 多层租户 / 多应用 启用 {@link HoI#child}
 *       - 多租户 / 多应用
 * </code></pre>
 * 应用环境直接对接 {@link io.macrocosm.specification.program.HArk} 配置容器（运行部分）
 * <pre><code>
 *     上述 {@link io.macrocosm.specification.program.HArk} 的引用可以直接在代码中使用
 *     {@link HAmbient#running} 的方式获取
 *      - {@link HAmbient#running()} 当前运行模式为 {@link io.horizon.eon.em.EmApp.Mode#CUBE}
 *        直接提取，此时系统环境中注册的应用只有一个
 *      - {@link HAmbient#running(String)} 当前运行模式为其他，需提供第二维度的内容执行应用环境提取
 *        此时系统环境中注册的应用可能存在多个
 * </code></pre>
 *
 * @author lang : 2023-06-07
 */
public interface HoI extends HBelong, Function<HoI, HoI> {
    /**
     * 多层租户标识中返回子租户标识信息
     *
     * @param id 子租户标识
     *
     * @return {@link HoI} 子租户标识信息
     */
    default HoI child(final String id) {
        return null;
    }

    default void child(final String id, final HoI hoi) {

    }
}
