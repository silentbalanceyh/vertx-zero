package io.macrocosm.specification.secure;

import io.horizon.specification.app.HBelong;

import java.util.Set;

/**
 * 「云租户域」边界 Cloud EmApp
 *
 * <hr/>
 * 该接口限定了云端租户的相关边界信息，且一个完整的云租户只会包含一个系统边界，边界和超级账号之间是
 * 1:1 的基本数量关系，且不允许一个超级账号拥有多个系统边界
 * <pre><code>
 *     1. 如果需要多个系统边界，可直接新开超级账号来完成
 *     2. 系统边界对应的就是一个现实租户中的所有资产相关信息
 * </code></pre>
 * 现阶段的版本中只包含了系统的基础信息：
 * <pre><code>
 *     1. 开发中心信息
 *     2. 应用中心信息
 * </code></pre>
 * 后续开其他数字化服务则可以考虑直接使用额外的数据结构来扩展。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HFrontier extends HBelong {
    /**
     * 当前 {@link HFrontier} 的子区域列表，它的子区域为平台租户，您可以直接通过此接口
     * 拿到所有 {@link HGalaxy} 的 ID 集合，三层应用之间使用软链接，在拓扑图中使用虚线
     * 标注。
     *
     * @return {@link Set} 集合
     */
    default Set<String> galaxies() {
        return Set.of();
    }
}
