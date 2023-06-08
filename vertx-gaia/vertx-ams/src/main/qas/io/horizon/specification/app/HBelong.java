package io.horizon.specification.app;

import io.horizon.annotations.reference.One2One;

/**
 * 「实体所属」
 * <hr/>
 * 和 HOwner 产生关联的抽象所属，三种区域都会继承自此接口实现拥有者的部署
 * <pre><code>
 *     1. 云租户所属，HFrontier
 *     2. 标准租户所属，HGalaxy
 *     3. 应用租户所属，HSpace
 * </code></pre>
 *
 * @author lang : 2023-05-20
 */
public interface HBelong {
    /**
     * 返回当前系统边界的拥有者的唯一标识符，使用此标识符可直接定位到「超级账号」，基本规范：
     * <pre><code>
     *    1. 该标识符必须是唯一的，且不可重复。
     *    2. 该标识符的唯一性不受 名空间 的影响，即它的唯一性是全局的。
     * </code></pre>
     *
     * @return 唯一标识符
     */
    @One2One("HOwner引用，" +
        "用于定位当前系统边界的拥有者，在现阶段核心版本中，一个超级账号只能拥有一个系统边界，" +
        "所以系统边界的拥有者 HOwner 应该是唯一的，返回的字符串则是账号唯一标识，该标识具备" +
        "全局唯一性。"
    )
    String owner();
}
