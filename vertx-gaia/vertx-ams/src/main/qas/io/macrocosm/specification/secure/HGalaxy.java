package io.macrocosm.specification.secure;

import io.horizon.specification.app.HBelong;

import java.util.Set;

/**
 * 「标准租户域」星云 Platform EmApp
 * <hr/>
 * 该接口限定了某一个云租户旗下所所有的所有平台信息，且一个完整的标准租户只会包含一个标准租户域，此租户
 * 和超级账号没有直接关系，但超级账号可以管理它旗下的所有平台。标准租户信息的实现流程可以遵循如下步骤：
 * <pre><code>
 *     1. 必须使用云租户域关联的两种账号执行初始化
 *        - 超级账号
 *        - 标准云租户账号
 *     2. 初始化完成之后，最终会生成当前标准住户的管理员账号以及凭证，后续所有行为都以此为起点
 * </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HGalaxy extends HBelong {
    /**
     * 当前标准租户域所属的云租户域标识，可读取唯一的云租户域，管理模式中所需
     *
     * @return 云租户域标识
     */
    String frontier();

    /**
     * 当前 {@link HGalaxy} 的子应用区域列表，它的子区域为应用账号，您可以通过此接口
     * 读取所有的 {@link HSpace} 空间 ID 集合，三层结构中使用软链接，拓扑图中使用虚
     * 线标注。
     *
     * @return {@link Set} 集合
     */
    default Set<String> spaces() {
        return Set.of();
    }
}
