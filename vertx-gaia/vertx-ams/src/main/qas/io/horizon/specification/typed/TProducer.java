package io.horizon.specification.typed;

import io.horizon.specification.unit.HLibrary;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 「工具契约」Construct
 * <hr/>
 * 工具契约从直接的组件契约中直接继承，用于描述工具基本属性，会多出
 * <pre><code>
 *     1. name() / 当前工具库名称
 *     3. dependencies() -> 反向依赖的标识集合
 *     4. metadata() / 当前工具映射
 * </code></pre>
 *
 * @author lang : 2023-05-21
 */
public interface TProducer extends TContract {
    /**
     * 当前工具库名称
     *
     * @return {@link String}
     */
    String name();

    /**
     * 当前工具的依赖库集合定义
     *
     * @return {@link Set<HLibrary>}
     */
    Set<HLibrary> dependencies();

    /**
     * 当前工具的元数据文件映射
     * <pre><code>
     *     MANIFEST -> ....
     *     LICENSE -> ....
     *     README -> ....
     *     ATOM -> ....
     *     ....
     * </code></pre>
     *
     * @return {@link ConcurrentMap}
     */
    ConcurrentMap<String, String> datum();
}
